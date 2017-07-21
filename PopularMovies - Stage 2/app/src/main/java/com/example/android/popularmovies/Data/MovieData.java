package com.example.android.popularmovies.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pamella on 26-Jun-17.
 */

public class MovieData implements Parcelable{
    private String id;
    private String title;
    private String imagePath;
    private String synopsis;
    private String rating;
    private String releaseDate;

    public MovieData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public MovieData(Parcel in){
        id = in.readString();
        title = in.readString();
        imagePath = in.readString();
        synopsis = in.readString();
        rating = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(imagePath);
        dest.writeString(synopsis);
        dest.writeString(rating);
        dest.writeString(releaseDate);
    }

    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>(){
        @Override
        public MovieData createFromParcel(Parcel source) {
            return new MovieData(source);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
}
