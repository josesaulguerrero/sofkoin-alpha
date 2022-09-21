package co.com.sofkoin.alpha.domain.user.entities;

import co.com.sofka.domain.generic.Entity;
import co.com.sofkoin.alpha.domain.user.values.ActivityTypes;
import co.com.sofkoin.alpha.domain.user.values.Timestamp;
import co.com.sofkoin.alpha.domain.user.values.identities.ActivityID;

public class Activity extends Entity<ActivityID> {
    private final Timestamp timestamp;
    private final ActivityTypes activityTypes;

    public Activity(ActivityID entityId, Timestamp timestamp, ActivityTypes activityTypes) {
        super(entityId);
        this.timestamp = timestamp;
        this.activityTypes = activityTypes;
    }

    public Timestamp timestamp() {
        return timestamp;
    }

    public ActivityTypes activityTypes() {
        return activityTypes;
    }
}
