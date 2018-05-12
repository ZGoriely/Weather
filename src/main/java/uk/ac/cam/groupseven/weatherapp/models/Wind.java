package uk.ac.cam.groupseven.weatherapp.models;

public class Wind {
    public final float speedMPS;
    public final String direction; /* Yes, I used a string. And what? I could have used an enum, but that would have been looong. We'll probs display as a string anyway..
                                      Okay, I'll switch it to an enum if it becomes necessary. */

    public Wind(float speed, String dir){
        this.speedMPS = speed;
        this.direction = dir;
    }

    @Override
    public String toString(){
        return "speedMPS: "+this.speedMPS+", direction: "+this.direction;
    }
}
