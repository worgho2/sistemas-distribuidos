from flask_sse import sse
from datetime import datetime, timedelta, timezone
from threading import Timer
from appointment import Appointment
from flask import Flask


class NotificationService():
    def __init__(self, app: Flask) -> None:
        self.app = app
        self.timer_tasks: dict[str, Timer] = {}

    def __get_timer_task_id(self, client_name: str, appointment_name: str):
        return f"{appointment_name}|{client_name}"

    def __get_timer_task_interval(self, date: datetime, reminder: str):
        minutes_to_subtract = 0

        if reminder == "FIVE_MINUTES_BEFORE":
            minutes_to_subtract = 5
        elif reminder == "TEN_MINUTES_BEFORE":
            minutes_to_subtract = 10

        date_with_offset = date - timedelta(minutes=minutes_to_subtract)
        now = datetime.now(timezone.utc)

        interval = (date_with_offset - now).total_seconds()

        if interval >= 0:
            return interval
        else:
            return 0

    def __send_appointment_reminder(self, client_name: str, appointment: Appointment, reminder: str):
        print(f"Sending {appointment.name} reminder to {client_name}")

        self.app.app_context().push()
        sse.publish({
            "name": appointment.name,
            "date": appointment.date,
            "owner": appointment.owner,
            "reminder": reminder
        }, type=f"reminders-{client_name}")

    def schedule_appointment_reminder(self, client_name: str, appointment: Appointment, reminder: str):
        if reminder == "DISABLED":
            return

        timer_id = self.__get_timer_task_id(client_name, appointment.name)
        timer_interval = self.__get_timer_task_interval(
            appointment.date, reminder)

        timer = Timer(timer_interval, self.__send_appointment_reminder,
                      [client_name, appointment, reminder])

        self.timer_tasks.update({timer_id: timer})
        self.timer_tasks.get(timer_id).start()

    def cancel_appointment_all_reminders(self, appointment_name: str):
        keys = list(self.timer_tasks.keys())
        for task_id in keys:
            if task_id.startswith(appointment_name):
                self.timer_tasks.get(task_id).cancel()
                self.timer_tasks.pop(task_id)

    def cancel_appointment_reminder(self, client_name: str, appointment_name: str):
        task_id = self.__get_timer_task_id(client_name, appointment_name)
        keys = list(self.timer_tasks.keys())
        if task_id in keys:
            self.timer_tasks.get(task_id).cancel()
            self.timer_tasks.pop(task_id)

    def update_appointment_reminder(self, client_name: str, appointment: Appointment, reminder: str):
        self.cancel_appointment_reminder(client_name, appointment.name)
        self.schedule_appointment_reminder(client_name, appointment, reminder)

    def send_appointment_invite(self, client_name: str, appointment: Appointment, signature: str):
        print(f"Sending {appointment.name} invite to {client_name}")

        sse.publish({
            "name": appointment.name,
            "date": appointment.date,
            "owner": appointment.owner,
            "signature": signature
        }, type=f"invites-{client_name}")
