
/*
    Connor Newbery
    V00921506
 */


#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <stdbool.h>
#include "emalloc.h"
#include "ics.h"
#include "listy.h"

#define MAX_LINE_LEN 132
 
node_t  *parse(FILE *file);
node_t  *reccur_helper(node_t *evptr);
event_t  reccur(node_t *temp, char *date, char *edate);
void     dt_increment(char *after, const char *before, int const num_days);
node_t  *sort(node_t *evlist);
node_t  *dtf_helper(node_t *evlist);
node_t  *date_format(node_t *event);
void     print_events(node_t *eventlist, int from_yy, int from_mm, int from_dd, 
    int to_yy, int to_mm, int to_dd);

int main(int argc, char *argv[])
{
    int    from_y     = 0, from_m = 0, from_d = 0;
    int    to_y       = 0, to_m = 0, to_d = 0;
    char   *filename  = NULL;
    int    i; 
    node_t *eventlist = NULL;

    //collect the filename and start and end date parameters from the command line
    for (i = 0; i < argc; i++) {
        if (strncmp(argv[i], "--start=", 8) == 0) {
            sscanf(argv[i], "--start=%d/%d/%d", &from_y, &from_m, &from_d);
        } else if (strncmp(argv[i], "--end=", 6) == 0) {
            sscanf(argv[i], "--end=%d/%d/%d", &to_y, &to_m, &to_d);
        } else if (strncmp(argv[i], "--file=", 7) == 0) {
            filename = argv[i]+7;
        }
    }

    //if there are no arguments, or arguments given incorrectly, exit
    if (from_y == 0 || to_y == 0 || filename == NULL) {
        fprintf(stderr, 
            "usage: %s --start=yyyy/mm/dd --end=yyyy/mm/dd --file=icsfile\n",
            argv[0]);
        exit(1);
    }

    //open the file provided from the command line
    FILE *file;
    file = fopen(filename, "r");
    //error check
    if(file == NULL){
        printf("File empty, or unaccessible");
    }

    //Parse the file
    eventlist = parse(file);

    //Create weekly events for the reccuring events
    eventlist = reccur_helper(eventlist);

    //Format the dates properly
    eventlist = dtf_helper(eventlist);

    //Print the events
    print_events(eventlist, from_y, from_m, from_d, to_y, to_m, to_d);
    
    fclose(file);

    exit(0);
}

/*
    The parse function takes the file provided in the command line, parses, and distributes its content into 
    event_t structs that are then added onto a linked list.
*/
node_t *parse(FILE *file){

    char    *garbage     = emalloc(sizeof(char) * MAX_LINE_LEN);
    char    *fil         = emalloc(sizeof(char) * MAX_LINE_LEN);
    node_t  *nodepointer = calloc(1,sizeof(node_t));
    node_t **pointer     = &nodepointer;
    event_t  event;

    //loops through the provided file line by line, copying the content of the file into the appropriate event_t struct parameter
    while(fgets(fil, MAX_LINE_LEN, file) != NULL){

        //collect the event's start date
        if(strstr(fil,"DTSTART") != NULL)
        {  
            sscanf(fil, "%[^:]:%s", garbage, event.dtstart);
            sscanf(event.dtstart, "%[^T]T%s",event.start_d, event.start_t);
        }

        //Collect the event's end date
        if(strstr(fil,"DTEND") != NULL)
        { 
            sscanf(fil, "%[^:]:%s", garbage, event.dtend);
            sscanf(event.dtend, "%[^T]T%s",event.end_d, event.end_t);
        }

        //Collect the event's reccuring rrule
        if(strstr(fil, "RRULE") != NULL)
        {
            char *t;
            t = strtok(fil, ";");
            while(t){
                if(strstr(t, "UNTIL")){
                    sscanf(t, "%[^=]=%s", garbage, event.rrule);
                }
                t = strtok(NULL, ";");
            }
        }

        //Collect the event's location data
        if(strstr(fil, "LOCATION") != NULL)
        {
            sscanf(fil, "%[^:]:%[^\n]", garbage, event.location);
        }

        //Collect the event's summary data
        if(strstr(fil, "SUMMARY") != NULL)
        {
            sscanf(fil, "%[^:]:%[^\n]", garbage, event.summary);
        }

        //If the end of the event is reached, add the event to the list
        if(strstr(fil,"END:VEVENT") != NULL)
        {
            *pointer = add_front(*pointer, new_node(event));
            //reset the contents of the event struct once the event has been added to the list, so as not to hold values from previous events
            memset(&event, 0, sizeof(event));
        }
        
    }

    free(garbage);
    free(fil);
    return nodepointer;
}
/*
    reccur_helper is used as to call the reccur function
    the function iterates over the event list and calls "reccur" when there is a "RRULE" present 
    in any given event.  Reccur is called repeatedly until the start date is greater than or
    equal to the date given by the RRULE in the event.
*/
node_t *reccur_helper(node_t *evptr){
    node_t **timp = &evptr;
    node_t  *temp = evptr;
    char    *date = NULL, *edate = NULL;
    event_t  hold;

    //the outer loop, to iterate over the list
   for( ; temp != NULL; temp = temp->next){

        //check if the event is reccurring
        if(strcmp(temp->val.rrule, "") != 0){
            date = emalloc(sizeof(char)*DT_LEN);
            edate = emalloc(sizeof(char)*DT_LEN);

            //copy the list head's start and end date into the temporary character arrays
            strcpy(date, temp->val.dtstart);
            strcpy(edate, temp->val.dtend);
            hold = reccur(temp, date, edate);
            *timp = add_front((*timp), new_node(hold));

            //inner while loop to call the function reccur repeatedly
            while(strncmp((*timp)->val.dtstart, temp->val.rrule, 8) < 0){

                //copy the new head's start and end date into the temporary character arrays
                strcpy(date, (*timp)->val.dtstart);
                strcpy(edate, (*timp)->val.dtend);
                hold = reccur(temp, date, edate);
                *timp = add_front((*timp), new_node(hold));
                memset(&hold, 0, sizeof(hold));
            }
            //if the head of the list went beyond the rrule date, remove it
            if(strncmp((*timp)->val.dtstart, temp->val.rrule, 8) > 0){
                node_t *freer = (*timp);
                *timp = remove_front(*timp);
                free(freer);
            }

            free(date);
            free(edate);

        }        
    }

    return evptr;

}
/*

    The function reccur creates a new event, with exactly the same data as the present event
    in the function reccur_helper, except the start and end dates are incremented by one week.
    the new event is thend added onto the front of the event list.

*/

event_t reccur(node_t *temp, char *date, char *edate){
    char *after_increment = emalloc(sizeof(char)*MAX_LINE_LEN);
    char *end_increment   = emalloc(sizeof(char)*MAX_LINE_LEN);

    //initialize a new event that copies all the date from the present event in reccur helper
    event_t hold = temp->val;
    strcpy(hold.dtstart, date);
    strcpy(hold.dtend, edate);

    //increment the start date in the hold event and copy the values back into the hold event
    dt_increment(after_increment, hold.dtstart, 7);
    strncpy(hold.dtstart, after_increment, strlen(after_increment));
    sscanf(after_increment, "%[^T]T%s", hold.start_d, hold.start_t);

    //increment the end date in the hold event and copy the values back into the hold event
    dt_increment(end_increment, hold.dtend, 7);
    strncpy(hold.dtend, end_increment, strlen(after_increment));
    sscanf(end_increment, "%[^T]T%s", hold.end_d, hold.end_t);

    free(after_increment);
    free(end_increment);
    
    return hold;
    
}

void dt_increment(char *after, const char *before, int const num_days)
{
    struct tm temp_time;
    time_t    full_time;
    
    //empty the time struct, then fill it with the values from the given event date, and increment the day by 7
    memset(&temp_time, 0, sizeof(struct tm));
    sscanf(before, "%4d%2d%2dT%2d%2d%2d", &temp_time.tm_year,
        &temp_time.tm_mon, &temp_time.tm_mday, &temp_time.tm_hour, 
        &temp_time.tm_min, &temp_time.tm_sec);
    temp_time.tm_year -= 1900;
    temp_time.tm_mon -= 1;
    temp_time.tm_mday += num_days;

    //format the date and time and store it into the character array
    full_time = mktime(&temp_time);
    after[0] = '\0';
    strftime(after, 16, "%Y%m%dT%H%M%S", localtime(&full_time));
    strncpy(after + 16, before + 16, MAX_LINE_LEN - 16); 
    after[MAX_LINE_LEN - 1] = '\0';
}


/*
    The funcion dtf_helper takes the sorted event list and iterates through the list, sending each node to the 
    date_format function, so that the date and time can be properly formatted before printing.
*/
node_t *dtf_helper(node_t *evlist){

    //Allocate memory for a new head node to create a new lsit
    node_t  *new_list;
    node_t **evlist_ptr = &new_list;
    node_t  *temp = evlist;

    //iterate through the old, unformatted list, and add the formatted event onto the new list in order
    while(temp != NULL){
        temp = date_format(temp);
       *evlist_ptr = add_inorder(*evlist_ptr, new_node(temp->val));
        temp = temp->next;
    }

    //Free the memory held by the old list
    node_t *freer = NULL;
    for( ; evlist != NULL; evlist = freer){
        freer = evlist->next;
        free(evlist);
    }

    return new_list;
}
/*
    date_format is used to format the date and time for each event such that it prints in a readable fashion
*/
node_t *date_format(node_t *event){

    char     *after = emalloc(sizeof(char)*MAX_LINE_LEN);
    struct tm temp_time;
    time_t    full_time;

    //fill the time struct with the start date and time found in the event
    memset(&temp_time, 0, sizeof(struct tm));
    sscanf(event->val.dtstart, "%4d%2d%2dT%2d%2d%2d", &temp_time.tm_year,
        &temp_time.tm_mon, &temp_time.tm_mday, &temp_time.tm_hour, 
        &temp_time.tm_min, &temp_time.tm_sec);
    temp_time.tm_year -= 1900;
    temp_time.tm_mon -= 1;

    full_time = mktime(&temp_time);

    //format the date and time, and copy them into the event
    strftime(event->val.f_sdate, MAX_LINE_LEN, "%B %d, %Y (%a)", localtime(&full_time));
    strftime(event->val.f_stime, MAX_LINE_LEN, "%I:%M %p", localtime(&full_time));

    //Replace leading zeros with a space
    if(strncmp(event->val.f_stime, "0", 1) == 0){
        event->val.f_stime[0] = ' ';
    }

    //fill the time struct with the end date and time found in the event
    memset(&temp_time, 0, sizeof(struct tm));
    sscanf(event->val.dtend, "%4d%2d%2dT%2d%2d%2d", &temp_time.tm_year,
        &temp_time.tm_mon, &temp_time.tm_mday, &temp_time.tm_hour, 
        &temp_time.tm_min, &temp_time.tm_sec);
    temp_time.tm_year -= 1900;
    temp_time.tm_mon -= 1;

    full_time = mktime(&temp_time);

    //format the date and time and copy them into the event
    strftime(event->val.f_etime, MAX_LINE_LEN, "%I:%M %p", localtime(&full_time));
    if(strncmp(event->val.f_etime, "0", 1) == 0){
        event->val.f_etime[0] = ' ';
    }

    free(after);

    return event;
}

/*
    The print event functions prints the events from the sorted list, that fall between the dates given
    via the command line
*/
void print_events(node_t *eventlist, int from_yy, int from_mm, int from_dd, 
    int to_yy, int to_mm, int to_dd){

    //create the start and cutoff dates from the values given from the command line
    int from = from_yy*1000+from_mm*100+from_dd;
    int to   = to_yy*1000+to_mm*100+to_dd;

    //if the month or day starts with a 0, add an extra 0
    if(from < 20000000)
        from = from_yy*10000 + from_mm*100 + from_dd;
    if(to < 20000000)
        to = to_yy*10000 + to_mm*100 + to_dd;


    node_t *tempy = eventlist;
    char   *t = emalloc(sizeof(char)*MAX_LINE_LEN);
    int     prev_date = 0, i =0;

    //iterate over the list
    while(tempy != NULL){

        //create integer values for the start and end time
        int start = atoi(tempy->val.start_d);
        strncpy(t, tempy->val.dtend, 8);
        int end = atoi(t);

        //use the integer values created above to compare the event's start and end time with the start and cutoff time given at the command line
        if(start >= from && end <= to){

            //create the required amount of dashes to follow the date
            for(int i = 0; i < strlen(tempy->val.f_sdate); i++){
                strcat(tempy->val.dashes, "-");
            }

            //If the current date is the same as the previous event's date, do not reprint the date
            if(prev_date == start){
                printf("%s to %s: %s {{%s}}\n", tempy->val.f_stime, tempy->val.f_etime, tempy->val.summary, tempy->val.location);
            }

            //if the previous event has a different date, print the entire event, with the date
            else{

                //if its the first event to be printed, print the event without a space above it
                if(i == 0){
                    printf("%s\n", tempy->val.f_sdate);
                    printf("%s\n", tempy->val.dashes);
                    printf("%s to %s: %s {{%s}}", tempy->val.f_stime, tempy->val.f_etime, tempy->val.summary, tempy->val.location);
                    printf("\n");
                }

                //if its not the first event to be printed, print the event with a space above it
                else if(i!=0){
                    printf("\n");
                    printf("%s\n", tempy->val.f_sdate);
                    printf("%s\n", tempy->val.dashes);
                    printf("%s to %s: %s {{%s}}", tempy->val.f_stime, tempy->val.f_etime, tempy->val.summary, tempy->val.location);
                    printf("\n");
                }
            }
            i++;
        }
        prev_date = start;
        tempy = tempy->next;
    }

    //free the memory used by the list
    free(t);
    node_t *freer = NULL;
    for( ; eventlist != NULL; eventlist = freer){
        freer = eventlist->next;
        free(eventlist);
    }
}
