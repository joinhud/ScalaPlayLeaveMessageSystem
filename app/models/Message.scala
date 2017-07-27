package models

import java.time.LocalDateTime

/*
* Model for message
*/
case class Message(userName: String, messageText: String, date: LocalDateTime)
