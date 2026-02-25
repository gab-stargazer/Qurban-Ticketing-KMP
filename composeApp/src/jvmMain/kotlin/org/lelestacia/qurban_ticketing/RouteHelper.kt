package org.lelestacia.qurban_ticketing

import org.lelestacia.qurban_ticketing.util.route.UserList

fun List<Any>.isRoot(): Boolean {
    return this.last() in listOf(UserList)
}