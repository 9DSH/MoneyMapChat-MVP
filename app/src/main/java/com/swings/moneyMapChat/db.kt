package com.swings.moneyMapChat

import com.swings.moneyMapChat.models.Category
import com.swings.moneyMapChat.models.Expense
import com.swings.moneyMapChat.profile.UserProfile
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

val config = RealmConfiguration.create(schema = setOf(Expense::class, Category::class, UserProfile::class))
val db: Realm = Realm.open(config)

// , UserProfile::class
