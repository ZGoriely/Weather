package uk.ac.cam.groupseven.weatherapp.models;


import org.jetbrains.annotations.Nullable;

public class Wind {
    @Nullable
    public final Float speedMPS;
    @Nullable
    public final String direction; /* Yes, I used a string. And what? I could have used an enum, but that would have been looong. We'll probs display as a string anyway..
                                      Okay, I'll switch it to an enum if it becomes necessary. */

    public Wind(@Nullable Float speed, @Nullable String direction) {
        this.speedMPS = speed;
        this.direction = direction;
    }

    @Override
    public String toString(){
        return "speedMPS: "+this.speedMPS+", direction: "+this.direction;
    }
}
