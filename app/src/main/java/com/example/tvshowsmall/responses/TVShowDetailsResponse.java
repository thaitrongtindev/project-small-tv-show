package com.example.tvshowsmall.responses;

import com.example.tvshowsmall.models.TVShowDetails;
import com.google.gson.annotations.SerializedName;

public class TVShowDetailsResponse {

    @SerializedName("tvShow")
    private TVShowDetails tvShowDetails;

    public TVShowDetailsResponse(TVShowDetails tvShowDetails) {
        this.tvShowDetails = tvShowDetails;
    }

    public TVShowDetails getTvShowDetails() {
        return tvShowDetails;
    }

    public void setTvShowDetails(TVShowDetails tvShowDetails) {
        this.tvShowDetails = tvShowDetails;
    }
}
