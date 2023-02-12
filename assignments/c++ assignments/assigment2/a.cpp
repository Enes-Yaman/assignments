#include <string>
#include <fstream>
#include <vector>
#include <iostream>
#include "a.h"

using namespace std;

static Apartment *head = nullptr;
ofstream outputWriter;

apartmentptr find_Apartment(basic_string<char> &apartmentName);

houseptr find_FlatByName(apartmentptr pApartment, basic_string<char> &name);

houseptr find_FlatByIndex(apartmentptr pApartment, int i);

int findUsingBandwidth(apartmentptr pApartment);

int main(int argc, char **argv) {
    string input = argv[1];
    string output = argv[2];
    remove(output.data());
    outputWriter.open(output.data(), ios::app);
    inputReader(input);
    outputWriter.close();
    return 0;
}

void inputReader(string &inputFile) {
    // reads the data and send them to function caller for categorize
    string keyString;
    ifstream keyStream;
    keyStream.open(inputFile);
    if (keyStream.is_open()) {
        while (getline(keyStream, keyString)) {
            if (keyString.find('\n') != string::npos || keyString.find('\r') != string::npos) {
                keyString.pop_back();
            }
            vector<string> line = split(keyString, '\t');
            functionCaller(line);
        }
        keyStream.close();
    }
}

vector<string> split(string &basicString, char splitter) {
    // basic splitter
    string temp;
    vector<string> result;
    for (char i: basicString) {
        if (i != splitter) {
            temp.push_back(i);
        } else {
            result.push_back(temp);
            temp.clear();
        }
    }
    result.push_back(temp);
    return result;
}

void functionCaller(vector<string> commandLine) {
    // gets the command line and call the function to do
    if (commandLine[0] == "add_apartment") {
        add_apartment(commandLine[1], commandLine[2], commandLine[3]);
    } else if (commandLine[0] == "add_flat") {
        add_flat(commandLine[1], commandLine[2], commandLine[3], commandLine[4]);
    } else if (commandLine[0] == "remove_apartment") {
        remove_apartment(commandLine[1]);
    } else if (commandLine[0] == "merge_two_apartments") {
        merge_two_apartments(commandLine[1], commandLine[2]);
    } else if (commandLine[0] == "find_sum_of_max_bandwidths") {
        find_sum_of_max_bandwidths();
    } else if (commandLine[0] == "list_apartments") {
        list_apartments();
    } else if (commandLine[0] == "make_flat_empty") {
        make_flat_empty(commandLine[1], commandLine[2]);
    } else if (commandLine[0] == "relocate_flats_to_same_apartment") {
        relocate_flats_to_same_apartment(commandLine[1], commandLine[2], commandLine[3]);
    }
}

apartmentptr newApartment(int maxBandwith, string apartmentName) {
    apartmentptr temp = new Apartment;
    temp->name = apartmentName;
    temp->max_bandwith = maxBandwith;
    temp->house = nullptr;
    return temp;
}

houseptr newHouse(int id, int initial_bandwidth) {
    houseptr temp = new House();
    temp->id = id;
    temp->initial_bandwidth = initial_bandwidth;
    return temp;
}

void add_apartment(basic_string<char> &apartmentName, basic_string<char> &position, basic_string<char> &maxBandwith) {
    // adds the apartment to the linked-list
    if (position == "head") {
        apartmentptr temp = newApartment(stoi(maxBandwith), apartmentName);
        if (head != nullptr) {
            temp->next = head;
            for (apartmentptr temp2 = head; true; temp2 = temp2->next) {
                if (temp2->next == head) {
                    temp2->next = temp;
                    break;
                }
            }
        } else {
            head = temp;
            head->next = head;
        }

    } else {
        vector<string> position_vector = split(position, '_');
        if (position_vector[0] == "before") {
            for (apartmentptr ptr = head; true; ptr = ptr->next) {
                if (ptr->next->name == position_vector[1]) {
                    apartmentptr ptr1 = newApartment(stoi(maxBandwith), apartmentName);
                    if (head == ptr->next) {
                        head = ptr1;
                    }
                    ptr1->next = ptr->next;
                    ptr->next = ptr1;

                    break;
                }
            }
        } else if (position_vector[0] == "after") {
            apartmentptr ptr = find_Apartment(position_vector[1]);
            apartmentptr ptr1 = newApartment(stoi(maxBandwith), apartmentName);
            ptr1->next = ptr->next;
            ptr->next = ptr1;
        }
    }
}

void add_flat(basic_string<char> &apartmentName, basic_string<char> &index, basic_string<char> &initialBandwith,
              basic_string<char> &flatId) {
    // check the apartment and if there is an apartment add a new flat to that apartman
    int bandwith = stoi(initialBandwith);
    apartmentptr findedApartment = find_Apartment(apartmentName);
    houseptr findedHouse = find_FlatByIndex(findedApartment, stoi(index));
    houseptr prevHouse = find_FlatByIndex(findedApartment, stoi(index) - 1);
    int usingBandwith = findUsingBandwidth(findedApartment);
    if (usingBandwith + bandwith > findedApartment->max_bandwith) {
        bandwith = findedApartment->max_bandwith - usingBandwith;
    }
    houseptr newHouseptr = newHouse(stoi(flatId), bandwith);
    if (!findedApartment->house) {
        findedApartment->house = newHouseptr;
    } else if (!findedHouse) {
        prevHouse->next = newHouseptr;
        newHouseptr->prev = prevHouse;
    } else {
        if (stoi(index) == 0) {
            findedApartment->house = newHouseptr;
            newHouseptr->prev = nullptr;
            newHouseptr->next = findedHouse;
            findedHouse->prev = newHouseptr;
        } else {
            prevHouse->next = newHouseptr;
            newHouseptr->prev = prevHouse;
            newHouseptr->next = findedHouse;
            findedHouse->prev = newHouseptr;
        }
    }

}

int findUsingBandwidth(apartmentptr pApartment) {
    //basic calculation for using bandwidth
    houseptr tempHouse = pApartment->house;
    if (!tempHouse) {
        return 0;
    }
    int tempBandwith = 0;
    do {
        tempBandwith += tempHouse->initial_bandwidth;
        tempHouse = tempHouse->next;
    } while (tempHouse);
    return tempBandwith;
}

houseptr find_FlatByIndex(apartmentptr pApartment, int i) {
    // finding flat by index for adding flat
    if (pApartment->house) {
        houseptr temp = pApartment->house;
        int x = 0;
        while (x < i) {
            temp = temp->next;
            x++;
        }
        return temp;
    } else { return nullptr; }
}

void remove_apartment(basic_string<char> &apartmentName) {
    // removes apartment to the linked-list
    apartmentptr apartmentptr1 = find_Apartment(apartmentName);
    if (apartmentptr1 == head) {
        head = apartmentptr1->next;
    }
    if (apartmentptr1->house) {
        houseptr iterator = apartmentptr1->house;
        houseptr next = nullptr;
        while (iterator) {
            next = iterator->next;
            free(iterator);
            iterator = next;
        }
        free(iterator);
    }
    for (apartmentptr temp = head; true; temp = temp->next) {
        if (temp->next == apartmentptr1) {
            temp->next = apartmentptr1->next;
            free(apartmentptr1);
            break;
        }
    }
}

houseptr find_FlatByName(apartmentptr pApartment, basic_string<char> &name) {
    // finder flat by name for relocating and emptying
    int flatName = stoi(name);
    houseptr temp = pApartment->house;
    do {
        if (temp->id == flatName) {
            return temp;
        }
        temp = temp->next;
    } while (temp);
    return nullptr;
}

apartmentptr find_Apartment(basic_string<char> &apartmentName) {
    // apartment finder
    apartmentptr temp = head;
    do {
        if (temp->name == apartmentName) {
            return temp;
        }
        temp = temp->next;
    } while (temp != head);
    return nullptr;
}

void make_flat_empty(basic_string<char> &apartmentName, basic_string<char> &flatId) {
    //makes the flat empty
    apartmentptr apartmentptr1 = find_Apartment(apartmentName);
    houseptr houseptr1 = find_FlatByName(apartmentptr1, flatId);
    houseptr1->is_empty = true;
    houseptr1->initial_bandwidth = 0;
}

void find_sum_of_max_bandwidths() {
    // find to talal bandwidths for all apartments
    int total = head->max_bandwith;
    for (apartmentptr ptrA = head->next; ptrA != head; ptrA = ptrA->next) {
        total += ptrA->max_bandwith;
    }
    outputWriter << "sum of bandwidth: " << total << endl;
    outputWriter << endl;
}

void merge_two_apartments(basic_string<char> &apartmentName1, basic_string<char> &apartmentName2) {
    // merges the two apartments
    apartmentptr apartmentptr1 = find_Apartment(apartmentName1);
    apartmentptr apartmentptr2 = find_Apartment(apartmentName2);
    if (!apartmentptr1->house) {
        apartmentptr1->house = apartmentptr2->house;
        apartmentptr1->max_bandwith += apartmentptr2->max_bandwith;
        apartmentptr2->house = nullptr;
        remove_apartment(apartmentptr2->name);
    } else if (!apartmentptr2->house) {
        apartmentptr1->max_bandwith += apartmentptr2->max_bandwith;
        apartmentptr2->house = nullptr;
        remove_apartment(apartmentptr2->name);
    } else {
        houseptr temp = apartmentptr1->house;
        for (; temp->next; temp = temp->next);
        temp->next = apartmentptr2->house;
        apartmentptr2->house->prev = temp;
        apartmentptr1->max_bandwith += apartmentptr2->max_bandwith;
        apartmentptr2->house = nullptr;
        remove_apartment(apartmentptr2->name);
    }
}

void relocate_flats_to_same_apartment(basic_string<char> &newApartmentName, basic_string<char> &flatIdToShift,
                                      basic_string<char> &flatIdList) {
    apartmentptr ourApartment = find_Apartment(newApartmentName);
    houseptr shiftHouse = find_FlatByName(ourApartment, flatIdToShift);
    string array = flatIdList;
    array.erase(array.begin());
    array.erase(array.end() - 1);
    vector<string> flatList = split(array, ',');
    for (string flat: flatList) {
        apartmentptr tempApartmentPtr = head;
        do {
            houseptr tempHouseptr = find_FlatByName(tempApartmentPtr, flat);
            if (tempHouseptr && tempApartmentPtr != ourApartment) {
                if (!tempHouseptr->prev) {
                    tempApartmentPtr->house = tempHouseptr->next;
                    if (tempHouseptr->next) {
                        tempHouseptr->next->prev = nullptr;
                    }
                } else {
                    houseptr prev = tempHouseptr->prev;
                    houseptr next = tempHouseptr->next;
                    prev->next = next;
                    if (next) {
                        next->prev = prev;
                    }
                }
                tempHouseptr->next = nullptr;
                tempHouseptr->prev = nullptr;
                if (!shiftHouse->prev) {
                    ourApartment->house = tempHouseptr;
                    tempHouseptr->next = shiftHouse;
                    shiftHouse->prev = tempHouseptr;
                } else {
                    houseptr prev = shiftHouse->prev;
                    prev->next = tempHouseptr;
                    tempHouseptr->prev = prev;
                    tempHouseptr->next = shiftHouse;
                    shiftHouse->prev = tempHouseptr;
                }
                tempApartmentPtr->max_bandwith -= tempHouseptr->initial_bandwidth;
                ourApartment->max_bandwith += tempHouseptr->initial_bandwidth;
                break;
            }
            tempApartmentPtr = tempApartmentPtr->next;
        } while (tempApartmentPtr != head);

    }

}

void list_apartments() {
    // lister and writer for apartments
    apartmentptr ptrA = head;
    do {
        outputWriter << ptrA->name << "(" << ptrA->max_bandwith << ")";
        houseptr ptrH = ptrA->house;
        if (ptrH) {
            do {
                outputWriter << "\tFlat" << ptrH->id << "(" << ptrH->initial_bandwidth << ")";
                ptrH = ptrH->next;
            } while (ptrH);
        }
        ptrA = ptrA->next;
        outputWriter << endl;
    } while (ptrA != head);
    outputWriter << endl;

}
