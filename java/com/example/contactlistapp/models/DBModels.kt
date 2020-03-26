package com.example.contactlistapp.models

data class UserDetails(var userName: String, var userEmail: String?=null, var userPassword: String?=null, var userID: String)

data class ContactDetails(var contactID: String, var contactName: String, var nickName: String?=null,
                          var phoneticName: String?=null, var company: String?=null, var title: String?=null, var website: String?=null,
                          var notes: String?=null, var group: String?=null)
//, var displayImage: String?=null

data class NumberDetails(var contactID: String, var numType: String, var number: String)

data class IMDetails(var contactID: String, var imType: String, var imAddr: String)

data class IMPDatesDetails(var contactID: String, var dateType: String, var date: String)

data class EmailDetails(var contactID: String, var emailType: String, var email: String)

data class CircleDetails(var contactID: String, var circleType: String, var relation: String)

data class AddressDetails(var contactID: String, var addressType: String, var address: String)

data class CandidateDetails(var candidateID: String, var candidateName: String, var candidateEmail: String, var candidatePhone: String)

data class QuestionDetails(var question: String, var correctAnswer: String,
                           var wrongAnswer1: String, var wrongAnswer2: String, var wrongAnswer3: String,
                           var candidateSelection: String?=null)

