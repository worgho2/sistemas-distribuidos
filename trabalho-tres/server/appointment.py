from datetime import datetime


class Appointment(object):
    def __init__(self, name: str, date: datetime, owner: str, reminder: str, attendees: list[str]):
        self.name = name
        self.date = date
        self.owner = owner
        self.reminder = reminder
        self.attendeess = dict()

        for attendee in attendees:
            self.attendeess.update({attendee: 'PENDING'})

    def to_json(self):
        return {
            "name": self.name,
            "date": self.date.isoformat(),
            "owner": self.owner,
            "reminder": self.reminder,
            "attendees": list(self.attendeess.keys())
        }
