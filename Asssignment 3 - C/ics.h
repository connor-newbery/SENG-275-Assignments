
/*
    Connor Newbery
    V00921506
 */


#ifndef _ICS_H_
#define _ICS_H_

#define DT_LEN       17
#define SUMMARY_LEN  80
#define LOCATION_LEN 80
#define RRULE_LEN    80
#define TIME_LEN     7
#define DASH_LEN     80


typedef struct event_t
{
    char dtstart[DT_LEN];
    char dtend[DT_LEN];
    char summary[SUMMARY_LEN];
    char location[LOCATION_LEN];
    char rrule[RRULE_LEN];

    char start_d[DT_LEN];
    char start_t[TIME_LEN];
    char end_d[TIME_LEN];
    char end_t[TIME_LEN];

    char f_stime[DASH_LEN];
    char f_sdate[DASH_LEN];
    char f_etime[DASH_LEN];

    char dashes[DASH_LEN];
} event_t;

#endif
