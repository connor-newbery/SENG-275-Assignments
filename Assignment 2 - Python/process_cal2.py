#!/usr/bin/env python3
"""
CONNOR NEWBERY
V00921506

"""
import sys
from datetime import datetime, timedelta

def parse_file(str):
    fil = open(str)
    file = fil.read()

    event_list = []

    for line in file.split("END:VEVENT"):
        event_list.append(line)
    new_event_list = []
    for event in event_list:
        for line in event.split('\n'):
            if "DTSTART:" in line:
                start = line[-15:]
            if "DTEND:" in line:
                end = line[-6:]
            if "RRULE" in line:
                for c in line.split(';'):
                    if "UNTIL" in c:
                        rrule = c[6:20]
            if "LOCATION:" in line:
                loc = line[9:]
            if "SUMMARY:" in line:
                sum = line[8:] 
        if "RRULE:" not in event:
            rrule = ("") 
        new_event_list.append([start, end, rrule, loc, sum]) 
    new_event_list.pop()
    return new_event_list

def date_format(event, start_d, end_d):
    start = datetime.strptime(event[0], "%Y%m%dT%H%M%S")
    end = datetime.strptime(event[1],"%H%M%S")
    if event[2] is not "":
        rrule = datetime.strptime(event[2], "%Y%m%dT%H%M%S") 
    elif event[2] is "":
        rrule = ''
    dates = []
    temp_dt = []
    if rrule is '':
        event_dict = {
            "Location":event[3],
            "Summary":event[4],
            "Start": start,
            "EndTime":end,
            "Dates":dates
        }
        
    if rrule is not '':
        dates.append(start)
        delta = timedelta(days=7)

        temp = start
        while temp < rrule:
            dates.append(temp)
            temp = temp + delta        

    for date in dates:
        temp = date.strftime("%Y/%m/%d")
        temp = datetime.strptime(temp, "%Y/%m/%d")
        if temp >= start_d and temp <= end_d:
            temp_dt.append(date)
    dates.clear()
    dates = temp_dt
    event_dict = {
        "Location":event[3],
        "Summary":event[4],
        "Start": start,
        "EndTime":end,
        "Dates":dates
    }
    return event_dict
    

def event_dates(e_d_l):
    temp = {}
    for event in e_d_l:
        if not event["Dates"]:
            str_date = str(event["Start"])
            temp[str_date] = {
                    "Location": event["Location"],
                    "Summary": event["Summary"],
                    "Start": event["Start"], 
                    "End": event["EndTime"]
            }        
        else:    
            for date in event["Dates"]:
                str_date = str(date)
                temp[str_date] = {
                    "Location": event["Location"],
                    "Summary": event["Summary"],
                    "Start": date, 
                    "End": event["EndTime"]
                }

    res = sorted(temp.items(), key = lambda x: x[1]["Start"])       
    temp = {}
    for tuple in res:
        temp[tuple[0]] = tuple[1]
    return temp


def print_dates(event_dict, end_date, start_date):

    eventlist = list(event_dict)
    i = 0   
    j = 0
    for event in event_dict:
        temp = event_dict[event]["Start"].strftime("%Y/%m/%d")
        start = datetime.strptime(temp, "%Y/%m/%d")
        if start <= end_date and start >= start_date:
            if len(eventlist) > 1 and eventlist[i][:10] == eventlist[i-1][:10]:
                print(str(event_dict[event]["Start"].strftime("%l:%M %p")) + " to " + str(event_dict[event]["End"].strftime("%l:%M %p")) +":", end=" ")
                print(event_dict[event]["Summary"] + " {{" + event_dict[event]["Location"] + "}}")
            else:
                if j>0:
                    print()
                print(event_dict[event]["Start"].strftime("%B %d, %Y (%a)"))
                for c in event_dict[event]["Start"].strftime("%B %d, %Y (%a)"):
                    print("-", end="")
                print() 
                print(str(event_dict[event]["Start"].strftime("%l:%M %p")) + " to " + str(event_dict[event]["End"].strftime("%l:%M %p"))+":", end=" ")
                print(event_dict[event]["Summary"] + " {{" + event_dict[event]["Location"] + "}}")
                j = j+1
        i = i+1

def main():

    start_date = sys.argv[1]
    end_dt = sys.argv[2]
    filename = sys.argv[3]
    temp = []
    start_date = start_date.strip('--start=')
    end_dt = end_dt.strip('--end=')
    filename = filename.replace('--file=', '')
    st_date = datetime.strptime(start_date, "%Y/%m/%d")
    end_date = datetime.strptime(end_dt, "%Y/%m/%d")

    Events = parse_file(filename)
    for event in Events:
        temp.append(date_format(event, st_date, end_date))
    event_dict = event_dates(temp)
    print_dates(event_dict, end_date, st_date)

if __name__ == "__main__":
    main()


