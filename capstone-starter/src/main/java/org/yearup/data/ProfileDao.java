package org.yearup.data;


import org.yearup.models.Profile;

public interface ProfileDao
{
    Profile create(Profile profile);
    void update(Profile profile,int userId);
    Profile getByUserId(int userId);
}
