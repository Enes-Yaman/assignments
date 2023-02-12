//
// Created by rotroq on 11/30/22.
//

#ifndef ASSIGNMNET3_ORDERS_H
#define ASSIGNMNET3_ORDERS_H

class Orders {
private:
    double arrivalTime;
    double orderTime;
    double brewTime;
    double priceOfOrder;
    double turnAroundTime;
public:
    Orders(double aT, double oT, double bT, double pO) {
        arrivalTime = aT;
        orderTime = oT;
        brewTime = bT;
        priceOfOrder = pO;
    }

    double getArrivalTime() const;

    double getOrderTime() const;

    double getBrewTime() const;

    double getPriceOfOrder() const;

    Orders();

    double getTurnAroundTime() const;

    void setTurnAroundTime(double turnAroundTime);
};

#endif //ASSIGNMNET3_ORDERS_H
