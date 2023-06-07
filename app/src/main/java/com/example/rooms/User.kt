package com.example.rooms

class User {
var isim: String= "12"
    var kullanici_id: String = "null"
    var profil_resmi: String="null"
    var seviye: String = "null"
    var telefon: String? = "null"

    constructor(isim: String, kullanici_id: String, telefon: String, profil_resmi: String, seviye: String) {
        this.isim = isim
        this.kullanici_id = kullanici_id
        this.profil_resmi = profil_resmi
        this.seviye = seviye
        this.telefon = telefon
    }
    constructor (){}

}