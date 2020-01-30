/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.getyourguide.challenge.vo


import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("author")
    val author: Author? = Author(),
    @SerializedName("created")
    val created: String? = "", // 2019-03-29T14:54:03+01:00
    @SerializedName("enjoyment")
    val enjoyment: String? = "",
    @SerializedName("id")
    val id: Int? = 0, // 5283676
    @SerializedName("isAnonymous")
    val isAnonymous: Boolean? = false, // false
    @SerializedName("language")
    val language: String? = "", // en
    @SerializedName("message")
    val message: String? = "", // Well presented by Tom our guide for this excursion, being an Aviation Historian his knowledge was endless. The place itself is pretty spectacular standing out on the apron, looking up at the roof and ramparts which were to be a focal point for the Stadium roof.The tour itself flew by.
    @SerializedName("rating")
    val rating: Int? = 0, // 5
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("travelerType")
    val travelerType: String? = "" // friends
) : Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("author"),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    data class Author(
        @SerializedName("country")
        val country: String? = "", // United Kingdom
        @SerializedName("fullName")
        val fullName: String? = "", // Richard
        @SerializedName("photo")
        val photo: String? = "" // "https://cdn.getyourguide.com/img/customer_img-714889-786232894-11.jpg"
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        ) {
        }

        override fun writeToParcel(
            parcel: Parcel,
            flags: Int
        ) {
            parcel.writeString(country)
            parcel.writeString(fullName)
            parcel.writeString(photo)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Creator<Author> {
            override fun createFromParcel(parcel: Parcel): Author {
                return Author(parcel)
            }

            override fun newArray(size: Int): Array<Author?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(
        parcel: Parcel,
        flags: Int
    ) {
        parcel.writeString(created)
        parcel.writeString(enjoyment)
        parcel.writeValue(id)
        parcel.writeValue(isAnonymous)
        parcel.writeString(language)
        parcel.writeString(message)
        parcel.writeValue(rating)
        parcel.writeString(title)
        parcel.writeString(travelerType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<Review> {
        override fun createFromParcel(parcel: Parcel): Review {
            return Review(parcel)
        }

        override fun newArray(size: Int): Array<Review?> {
            return arrayOfNulls(size)
        }
    }

}
