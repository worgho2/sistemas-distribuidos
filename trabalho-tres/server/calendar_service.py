from datetime import datetime
from notification_service import NotificationService
from security_service import SecurityService
from appointment import Appointment


class CalendarService():
    def __init__(self, security_service: SecurityService) -> None:
        self.notification_service = NotificationService()
        self.security_service = security_service
        self.clients: list[str] = []
        self.appointments: list[Appointment] = []

    def register_user(self, client_name: str):
        if client_name not in self.clients:
            self.clients.append(client_name)

    def create_appointment(self, client_name: str, name: str, date: datetime, reminder: str, attendees: list[str]):
        if client_name not in self.clients:
            raise Exception("Client is not registered")

        filtered_attendees: list[str] = []

        for attendee in attendees:
            if attendee in self.clients and attendee != client_name and attendee not in filtered_attendees:
                filtered_attendees.append(attendee)

        appointment = Appointment(
            name=name,
            date=date,
            owner=client_name,
            reminder=reminder,
            attendees=filtered_attendees
        )

        self.appointments.append(appointment)

        self.notification_service.schedule_appointment_reminder(
            client_name=client_name,
            appointment=appointment,
            reminder=reminder
        )

        for attendee in filtered_attendees:
            signature = self.security_service.create_signature(
                client_name=attendee)

            self.notification_service.send_appointment_invite(
                client_name=attendee,
                appointment=appointment,
                signature=signature
            )

    def cancel_appointment(self, client_name: str, appointment_name: str):
        if client_name not in self.clients:
            raise Exception(f"Client {client_name} is not registered")

        for appointment in self.appointments:
            if appointment.name == appointment_name:
                if client_name == appointment.owner:
                    self.appointments = list(
                        filter(lambda x: x.name != appointment_name, self.appointments))
                    self.notification_service.cancel_appointment_all_reminders(
                        appointment_name)
                elif client_name in appointment.attendees.keys():
                    appointment.attendees.pop(client_name)
                    self.notification_service.cancel_appointment_reminder(
                        client_name, appointment_name)
                else:
                    raise Exception(
                        f"Client {client_name} is not in {appointment_name} appointment")

                return

        raise Exception(f"Appointment {appointment_name} was not found")

    def update_appointment_reminder(self, client_name: str, appointment_name: str, reminder: str):
        if client_name not in self.clients:
            raise Exception(f"Client {client_name} is not registered")

        for appointment in self.appointments:
            if appointment.name == appointment_name:
                if client_name == appointment.owner:
                    appointment.reminder = reminder
                    self.notification_service.update_appointment_reminder(
                        client_name=client_name,
                        appointment=appointment,
                        reminder=reminder
                    )
                elif client_name in appointment.attendees.keys():
                    appointment.attendees.update({client_name: reminder})
                    self.notification_service.update_appointment_reminder(
                        client_name=client_name,
                        appointment=appointment,
                        reminder=reminder
                    )
                else:
                    raise Exception(
                        f"Client {client_name} not in {appointment_name} appointment")

                return

        raise Exception(f"Appointment {appointment_name} was not found")

    def list_appointments(self, client_name: str):
        if client_name not in self.clients:
            raise Exception(f"Client {client_name} is not registered")

        client_appointments: list[Appointment] = []

        for appointment in self.appointments:
            if appointment.owner == client_name or (client_name in appointment.attendees.keys() and appointment.attendees.get(client_name) != 'PENDING'):
                client_appointments.append(appointment)

        return client_appointments

    def answer_appointment_invite(self, client_name: str, appointment_name: str, accept: bool, reminder: str):
        if client_name not in self.clients:
            raise Exception(f"Client {client_name} is not registered")

        for appointment in self.appointments:
            if appointment.name == appointment_name:
                if client_name in appointment.attendees.keys():
                    if appointment.attendees.get(client_name) == "PENDING":
                        if accept == True:
                            appointment.attendees.update(
                                {client_name: reminder})
                            self.notification_service.schedule_appointment_reminder(
                                client_name=client_name,
                                appointment=appointment,
                                reminder=reminder
                            )
                        else:
                            appointment.attendees.pop(client_name)
                    else:
                        raise Exception(
                            f"Client {client_name} has no pending invite for {appointment_name} appointment")
                else:
                    raise Exception(
                        f"Client {client_name} not in {appointment_name} appointment")

                return

        raise Exception(f"Appointment {appointment_name} was not found")
