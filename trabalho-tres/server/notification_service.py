from flask_sse import sse
from appointment import Appointment


class NotificationService():
    def schedule_appointment_reminder(self, client_name, appointment: Appointment, reminder: str):
        if reminder is "DISABLED":
            return

        # TODO: Schedule

        sse.publish({
            "name": appointment.name,
            "date": appointment.date,
            "owner": appointment.owner,
            "reminder": reminder
        }, type=f"reminders-{client_name}")

    def cancel_appointment_reminders(self, appointment: Appointment):
        # TODO: Schedule
        return

    def update_appointment_reminder(self, client_name: str, appointment: Appointment, reminder: str):
        # TODO: Schedule
        return

    def send_appointment_invite(self, client_name: str, appointment: Appointment, signature: str):
        sse.publish({
            "name": appointment.name,
            "date": appointment.date,
            "owner": appointment.owner,
            "signature": signature
        }, type=f"invites-{client_name}")
