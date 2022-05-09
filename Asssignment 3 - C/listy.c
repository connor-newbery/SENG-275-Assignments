
/*
    Connor Newbery
    V00921506
 */


/*
 * linkedlist.c
 *
 * Based on the implementation approach described in "The Practice 
 * of Programming" by Kernighan and Pike (Addison-Wesley, 1999).
 */

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "ics.h"
#include "emalloc.h"
#include "listy.h"


node_t *new_node(event_t val) {
    node_t *temp = emalloc(sizeof(node_t));

    temp->val = val;
    temp->next = NULL;

    return temp;
}


node_t *add_front(node_t *list, node_t *new) {
    new->next = list;
    list = new;
    return list;
}


node_t *add_end(node_t *list, node_t *new) {
    node_t *curr;

    if (list == NULL) {
        new->next = NULL;
        return new;
    }

    for (curr = list; curr->next != NULL; curr = curr->next);
    curr->next = new;
    new->next = NULL;
    return list;
}


node_t *peek_front(node_t *list) {
    return list;
}


node_t *remove_front(node_t *list) {
    if (list == NULL) {
        return NULL;
    }

    return list->next;
}
/*
    receives a pointer to the head of the list, receives a new node to be added to the list
    traverses the lists until it finds the correct placement (ie the next event has a greater start date & time
    and the previous event has a smaller start date and time) and inserts the event.
*/
node_t *add_inorder(node_t * list, node_t *new) {
    node_t *prev = NULL;
    node_t *curr = NULL;

    //if the list is empty, return the new node, as it is now the head of the list
    if (list == NULL) {
        return new;
    }
    //set curr to point to the head of the list
    curr = list;

    //Iterate over the given list while the new node's start date is less than the current node's
    while(curr != NULL && strcmp(new->val.start_d, curr->val.start_d) > 0) {
        
        //keep track of the current and previous node
        prev = curr;
        curr = curr->next;
    }

    //if curr is not at the end of the list, compare start times
    if(curr != NULL){

        //check if the new event and event pointed to by curr have the same start date
        if(strcmp(new->val.start_d, curr->val.start_d) == 0){
            int new_t = atoi(new->val.start_t);
            int current = atoi(curr->val.start_t);

            //continue to iterate over the list while the new start time is greater than the current event's
            while(curr != NULL && strcmp(new->val.start_d, curr->val.start_d) == 0 && new_t > current){

                //keep track of the current and previous node
                prev = curr;
                curr = curr->next;
            }
        }
    }

    //Have the new node point to curr
    new->next = curr;

    //If prev is NULL, return the new node, as it is now the head of the list
    if (prev == NULL) {
        return new;
    }       

    //Otherwise, set the previous node to point to the new node, and return the lsit
    else {
        prev->next = new;
        return list;
    }
}


void apply(node_t *list,
           void (*fn)(node_t *list, void *),
           void *arg)
{
    for ( ; list != NULL; list = list->next) {
        (*fn)(list, arg);
    }
}
