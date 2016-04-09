package com.example.kevin.fifastatistics.models.user;

/**
 * <b>Class:</b> Friend <br><br>
 * <b>Description:</b> <br>
 * The Friend class is meant to act as a 'stub' of a user, and should exist only
 * within instances of a User object. It defines only a few traits: <ul>
 * <li> <b>href</b>, a reference to the full user object represented by this friend
 * <li> <b>name</b>, the name of the user
 * <li> <b>imageUrl</b>, the URL of the user's image, and
 * <li> <b>level</b>, the user's level.
 * </ul>
 * @version 1.0
 * @author Kevin Grant
 *
 */
public class Friend
{
    private String id;
    private String name;
    private String imageUrl;
    private String registrationToken;
    private int level;

    @SuppressWarnings("unused")
    public Friend() {

    }

    private Friend(Builder builder)
    {
        this.id = builder.id;
        this.name = builder.name;
        this.imageUrl = builder.imageUrl;
        this.level = builder.level;
        this.registrationToken = builder.registrationToken;
    }

    public static class Builder
    {
        private String id;
        private String name;
        private String imageUrl;
        private String registrationToken;
        private int level;

        public Builder withId(String id)
        {
            this.id = id;
            return this;
        }

        public Builder withName(String name)
        {
            this.name = name;
            return this;
        }

        public Builder withImageUrl(String imageUrl)
        {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder withRegistrationToken(String registrationToken)
        {
            this.registrationToken = registrationToken;
            return this;
        }

        public Builder withLevel(int level)
        {
            this.level = level;
            return this;
        }

        public Friend build()
        {
            throwExceptionIfPropertiesAreNull();
            return new Friend(this);
        }

        private void throwExceptionIfPropertiesAreNull()
        {
            if (name == null) throwExceptionForProperty("name");
            else if (id == null) throwExceptionForProperty("id");
            else if (imageUrl == null) throwExceptionForProperty("imageUrl");
            else if (registrationToken == null) throwExceptionForProperty(
                    "registrationToken");
        }

        private void throwExceptionForProperty(String propertyName)
        {
            throw new IllegalArgumentException("ERROR! " + propertyName +
                    " cannot be null!");
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
