package com.example.movieexplorer.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "favorite_movies")
public class FavoriteMovie {
    @PrimaryKey
    private int movieId;

    private Date addedDate;
    private int sortOrder;

    public FavoriteMovie(int movieId, Date addedDate, int sortOrder) {
        this.movieId = movieId;
        this.addedDate = addedDate;
        this.sortOrder = sortOrder;
    }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public Date getAddedDate() { return addedDate; }
    public void setAddedDate(Date addedDate) { this.addedDate = addedDate; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}