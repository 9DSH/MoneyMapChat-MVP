package com.swings.moneyMapChat.profile


import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


class UserProfile : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var username: String = ""
    var monthlyIncome: Double = 0.0
    var occupation: String = ""
    var savingsGoal: Double = 0.0 // New field for savings goal amount
    var savingsGoalTimeframe: String = ""
}