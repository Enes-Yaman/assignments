//
// Created by enesyaman on 11/15/22.
//

#ifndef ASSIGNMENT_A_H
#define ASSIGNMENT_A_H

#include <string>
#include <vector>

using namespace std;

class a {

};

class House {
public:
    int id;
    int initial_bandwidth;
    bool is_empty;
    House *prev;
    House *next;
};

typedef struct House *houseptr;

houseptr newHouse(int id, int initial_bandwidth);

class Apartment {
public:
    int max_bandwith;
    string name;
    House *house;
    Apartment *next;
};

typedef struct Apartment *apartmentptr;

apartmentptr newApartment(int max_bandwith, string name);

void add_apartment(basic_string<char> &apartmentName, basic_string<char> &position, basic_string<char> &maxBandwith);

void remove_apartment(basic_string<char> &apartmentName);

void merge_two_apartments(basic_string<char> &apartmentName1, basic_string<char> &apartmentName2);

void add_flat(basic_string<char> &apartmentName, basic_string<char> &index, basic_string<char> &initialBandwith,
              basic_string<char> &flatId);

void find_sum_of_max_bandwidths();

void list_apartments();

void make_flat_empty(basic_string<char> &apartmentName, basic_string<char> &flatId);

void relocate_flats_to_same_apartment(basic_string<char> &newApartmentName, basic_string<char> &flatIdToShift,
                                      basic_string<char> &flatIdList);

void inputReader(string &inputFile);

vector<string> split(string &basicString, char splitter);

void functionCaller(vector<string> commandLine);

#endif //ASSIGNMENT_A_H
