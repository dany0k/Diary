package ru.vsu.cs.zmaev.diary.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note (
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,

    @ColumnInfo(name = "text")
    var text: String? = null,

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0,

    @ColumnInfo(name = "done")
    var done: Boolean = false,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte()
    ) {
        uid = parcel.readInt()
        text = parcel.readString()
        timestamp = parcel.readLong()
        done = parcel.readByte() != 0.toByte()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (uid != other.uid) return false
        if (text != other.text) return false
        if (timestamp != other.timestamp) return false
        if (done != other.done) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + done.hashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uid)
        parcel.writeString(text)
        parcel.writeLong(timestamp)
        parcel.writeByte(if (done) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}