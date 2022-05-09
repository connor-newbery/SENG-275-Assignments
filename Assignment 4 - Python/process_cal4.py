#!/usr/bin/env python3

######## Connor Newbery ########
########    V00921506   ########


import re
from datetime import datetime, timedelta


class process_cal:
    """process_cal is the class in which the problem soution is formulated"""

    def __init__(self, file):
        """the process_cal initializer function takes a file as a parameter and reads it into an instance of the process_cal class"""

        fil = open(file)
        self.file = fil.read()

    def get_events_for_day(self, date):
        """the get_events_for_day method acts as the "main" method for the process_cal class, 
        in that it is the method that is used to call other more functional mehtods"""

        eventlist = process_cal.parse_file(self)
        eventlist = process_cal.rrule(self, eventlist, date)
        eventlist = process_cal.event_organizer(self, eventlist, date)

        events = process_cal.event_string(self, eventlist, date)
        return events
    
    def parse_file(self):
        """the parse file method takes only the "self" variable, in order to access the file given from the tester file, 
        and to then parse that file into indivdual event objects"""

        #split the file using regex everywhere "END:EVENT" is found
        events = re.split("END:VEVENT", self.file)
        eventlist = []
        
        #build event objects for each "token" produced by splitting the file 
        for ev in events:

            #search the tokens for keywords
            start = re.search("DTSTART:(.*)", ev)
            end = re.search("DTEND:(.*)", ev)
            location = re.search("LOCATION:(.*)",ev)
            summary = re.search("SUMMARY:(.*)", ev)
            rrule = re.search("UNTIL=(.*);(.*)",ev)

            #create datetime objects for the start, end and rrule
            if(rrule):
                rrule = datetime.strptime(rrule.group(1), "%Y%m%dT%H%M%S")
            if start:
                start = datetime.strptime(start.group(1), "%Y%m%dT%H%M%S")
                end = datetime.strptime(end.group(1), "%Y%m%dT%H%M%S")

                #create a new event object
                new = Event(start, end, location.group(1), summary.group(1), rrule)

            #add the event to the eventlist
            eventlist.append(new)

        #remove the final event, as it is an empty event    
        eventlist.pop()
        return eventlist


    def rrule(self, eventlist, date):
        """the rrule method creates a new event for each weekly occurence 
        of the event, determined by the event's rrule"""

        new_list = []

        #create a new list with all of the new events created by the rrule of each event
        for event in eventlist:
            new_list.append(event)

            start = Event.get_start(event)
            rrule = Event.get_rrule(event)

            if rrule:
                new_start = start
                new_event = event

                while new_start < rrule:
                    new_event = Event.rrule_build(new_event)
                    new_start = Event.get_start(new_event)
                    if new_start < rrule:
                        new_list.append(new_event)

        return new_list

    
    def event_organizer(self, eventlist, date):
        """the event_organizer method sorts the complete eventlist by start date"""

        #use an anonymous function to create a new sorted listt, with the start date and time as the sorting key
        newlist = sorted(eventlist, key=lambda x: x.start)

        eventlist = []

        #go through the new, sorted list, and add only the events that occur on the date provided by the tester file to the eventlsit
        for event in newlist:
            start = Event.get_start(event)
            if start.date() == date.date():
                eventlist.append(event)

        return eventlist

    def event_string(self, eventlist, date):
        """the event_string method turns the final eventlist into the final formatted string that is to be sent back to the tester file"""

        if len(eventlist) is 0:
            return ""

        else:    
            #add the formatted start date
            events = ""
            events += date.strftime("%B %d, %Y (%a)") + "\n"

            #add the dashes
            for  c in date.strftime("%B %d, %Y (%a)"):
                events += "-"

            #add all of the events
            events += "\n"
            for event in eventlist:
                events += str(event) + "\n"
            
            #remove the final newline character
            events = events[:-1]
            return events

class Event:
        """the Event class is used to build event objects"""

        def __init__(self, start, end, location, summary, rrule):
            """the Event class initializer takes multiple arguments that are used to build instances of the event object"""

            self.start = start
            self.end = end
            self.location = location
            self.summary = summary
            if rrule is not None:
                self.rrule = rrule
            else:
                self.rrule = None
        
        def get_start(self):
            """the get_start method returns the value of the event's start date"""

            return self.start

        def get_rrule(self):
            """the get_rrule method returns the value of the event's rrule, if it exists"""

            return self.rrule

        def __repr__(self):
            """the __repr__ method returns a formatted string representation of the event"""

            return "%s to %s: %s {{%s}}" % (self.start.strftime("%l:%M %p"), self.end.strftime("%l:%M %p"), self.summary, self.location)

        def rrule_build(self):
            """the rrule_build method adds 7 days to the given 
            event's start date, and returns a new instance of the event"""

            delta = timedelta(days=7)
            return Event(self.start + delta, self.end, self.location, self.summary, self.rrule)
