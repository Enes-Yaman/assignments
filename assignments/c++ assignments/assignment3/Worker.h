//
// Created by rotroq on 11/30/22.
//

#ifndef ASSIGNMNET3_WORKER_H
#define ASSIGNMNET3_WORKER_H


class Worker {
public:
    double availableTime = 0;
    bool hasWork = false;
    Orders order;
    double busyTime = 0;
    PriorityOrderQueue *BaristaQueue = nullptr;
    Worker(PriorityOrderQueue *queue) {
        BaristaQueue = queue;
    }
    Worker(){
        Worker(nullptr);
    }
};

#endif //ASSIGNMNET3_WORKER_H
