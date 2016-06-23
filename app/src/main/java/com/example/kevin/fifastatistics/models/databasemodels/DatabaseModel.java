package com.example.kevin.fifastatistics.models.databasemodels;

/**
 * Represents a model that also has an ID assigned to it as a database key.
 * <p>
 * Can represent the full object that is present in collections in the database, such as a User
 * or Series object, or its stubbed form (e.g. Friend or MatchStub).
 */
public abstract class DatabaseModel {

    /**
     * Get the ID of the object.
     */
    public abstract String getId();

    /**
     * A DatabaseModel object is considered equal to another if their ID's are the same.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof  DatabaseModel) {
            DatabaseModel model = (DatabaseModel) object;
            if (model.getId().equals(getId())) {
                return true;
            }
        }
        return false;
    }
}
