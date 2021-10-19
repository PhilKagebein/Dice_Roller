package com.example.dice_Roller

import java.util.*

class DieModel(imageIcon: Int?, imageName: String?) {

    var imageIcon: Int? = imageIcon
    var imageName: String? = imageName
    var id: String? = UUID.randomUUID().toString()

    override fun toString(): String {
        return "DieModel(imageIcon=$imageIcon, imageName=$imageName, id=$id)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DieModel

        if (imageIcon != other.imageIcon) return false
        if (imageName != other.imageName) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = imageIcon ?: 0
        result = 31 * result + (imageName?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }

}