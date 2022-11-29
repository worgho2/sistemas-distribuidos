from flask import Flask, request, Response, jsonify
from flask_sse import sse
from flask_cors import CORS
from dateutil import parser
import json
from calendar_service import CalendarService
from security_service import SecurityService
import traceback

app = Flask(__name__)
CORS(app)
app.config["REDIS_URL"] = "redis://localhost"
app.register_blueprint(sse, url_prefix="/events/stream")
app.app_context().push()

security_service = SecurityService()
calendar_service = CalendarService(security_service=security_service, app=app)


@app.route("/users/register", methods=["POST"])
def register_user():
    try:
        body = request.get_json(force=True, cache=False)
        client_name = body.get('clientName', '')
        print(f"register_user | {body}")

        if client_name in (None, ''):
            raise Exception("Invalid request body")

        calendar_service.register_user(client_name=client_name)
        public_key = security_service.get_public_key()
        return jsonify({"publicKey": public_key}), 200
    except Exception as e:
        traceback.print_exc()
        return jsonify({"error": str(e)}), 400


@app.route("/users/<client_name>/appointments", methods=["POST"])
def create_appointment(client_name):
    try:
        body = request.get_json(force=True, cache=False)
        name = body.get('name', '')
        date = body.get('date', '')
        reminder = body.get('reminder', '')
        attendees = body.get('attendees', [])
        print(f"create_appointment | client_name: {client_name} | {body}")

        if name in (None, '') or date in (None, '') or reminder in (None, ''):
            raise Exception("Invalid request body")

        calendar_service.create_appointment(
            client_name=client_name,
            name=name,
            date=parser.parse(date),
            reminder=reminder,
            attendees=attendees
        )

        return jsonify({"status": "ok"}), 200
    except Exception as e:
        traceback.print_exc()
        return jsonify({"error": str(e)}), 400


@app.route("/users/<client_name>/appointments/<appointment_name>", methods=["DELETE"])
def cancel_appointment(client_name, appointment_name):
    try:
        print(
            f"cancel_appointment | client_name: {client_name} | appointment_name: {appointment_name}")

        calendar_service.cancel_appointment(
            client_name=client_name,
            appointment_name=appointment_name
        )

        return jsonify({"status": "ok"}), 200
    except Exception as e:
        traceback.print_exc()
        return jsonify({"error": str(e)}), 400


@app.route("/users/<client_name>/appointments/<appointment_name>/reminder", methods=["PATCH"])
def update_appointment_reminder(client_name, appointment_name):
    try:
        body = request.get_json(force=True, cache=False)
        reminder = body.get('reminder', '')
        print(
            f"update_appointment_reminder | client_name: {client_name} | appointment_name: {appointment_name} | {body}")

        if reminder in (None, ''):
            raise Exception("Invalid request body")

        calendar_service.update_appointment_reminder(
            client_name=client_name,
            appointment_name=appointment_name,
            reminder=reminder
        )

        return jsonify({"status": "ok"}), 200
    except Exception as e:
        traceback.print_exc()
        return jsonify({"error": str(e)}), 400


@app.route("/users/<client_name>/appointments", methods=["GET"])
def list_appointments(client_name):
    try:
        print(f"list_appointments | client_name: {client_name}")

        return Response(
            json.dumps(
                list(map(lambda x: x.to_json(), calendar_service.list_appointments(
                    client_name=client_name)))
            ),
            status=200,
            mimetype='application/json'
        )
    except Exception as e:
        traceback.print_exc()
        return jsonify({"error": str(e)}), 400


@app.route("/users/<client_name>/appointments/<appointment_name>/invite", methods=["PUT"])
def answer_appointment_invite(client_name, appointment_name):
    try:
        body = request.get_json(force=True, cache=False)
        accept = body.get('accept', '')
        reminder = body.get('reminder', '')
        print(
            f"answer_appointment_invite | client_name: {client_name} | appointment_name: {appointment_name} | {body}")

        if reminder in (None, '') or accept in (None, ''):
            raise Exception("Invalid request body")

        calendar_service.answer_appointment_invite(
            client_name=client_name,
            appointment_name=appointment_name,
            accept=accept is True,
            reminder=reminder
        )

        return jsonify({"status": "ok"}), 200
    except Exception as e:
        traceback.print_exc()
        return jsonify({"error": str(e)}), 400


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=3333)
