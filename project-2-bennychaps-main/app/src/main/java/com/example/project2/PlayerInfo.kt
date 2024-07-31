package com.example.project2

data class PlayerInfo(
    val lname: String,
    val fname: String,
    val team: String,
    val position: String,
    val id: String
){
    constructor() : this("", "", "", "", "")
}
