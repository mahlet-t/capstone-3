package org.yearup.data;


import org.springframework.security.core.parameters.P;
import org.yearup.models.Profile;

import java.util.List;

public interface ProfileDao
{
    Profile create(Profile profile);
    void update(Profile profile,int userId);
    Profile getByUserId(int userId);
}
