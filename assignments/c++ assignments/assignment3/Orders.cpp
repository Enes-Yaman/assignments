#include "Orders.h"

//
// Created by rotroq on 11/30/22.
//
double Orders::getArrivalTime() const {
    return arrivalTime;
}

double Orders::getOrderTime() const {
    return orderTime;
}

double Orders::getBrewTime() const {
    return brewTime;
}

double Orders::getPriceOfOrder() const {
    return priceOfOrder;
}

Orders::Orders() {

}

double Orders::getTurnAroundTime() const {
    return turnAroundTime;
}

void Orders::setTurnAroundTime(double turnAroundTime) {
    Orders::turnAroundTime = turnAroundTime;
}
