package com.studybuddy.sahilmahendrakar.studybuddy.gestures;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
//  Listens to gestures and calls methods accordingly. Designed to be subclassed
public class OnSwipeTouchListener implements View.OnTouchListener {
    //gesture detector for listening to events
    private GestureDetector gestureDetector;

    //assigns gesture listener to a new gesture detector
    public OnSwipeTouchListener(Context c) {
        gestureDetector = new GestureDetector(c, new GestureListener());
    }
    //called when the view is touched
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    //class determines what type of gesture was performed
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        //threshold for swipe distance
        private static final int SWIPE_THRESHOLD = 100;
        //swipe must be faster than this to count
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        //when the user touches event
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        //on click
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            onClick(); //calls on click
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleClick(); //when double tap
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongClick(); //on long press
            super.onLongPress(e);
        }

        // Determines the fling velocity and then fires the appropriate swipe event accordingly
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                //determines velocity
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    //checks direction and makes sure its over thresholds
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    //checks direction and makes sure its over thresholds
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    ///the following methods are called depending on what type of gesture is detected
    ///they will be implemented in subclasses
    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeUp() {
    }

    public void onSwipeDown() {
    }

    public void onClick() {

    }

    public void onDoubleClick() {

    }

    public void onLongClick() {

    }
}