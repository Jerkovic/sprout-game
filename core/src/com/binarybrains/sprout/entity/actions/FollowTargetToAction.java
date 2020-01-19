package com.binarybrains.sprout.entity.actions;

import com.binarybrains.sprout.entity.Entity;

public class FollowTargetToAction extends TemporalAction {

        private float startX, startY;
        private float endX, endY;

        private Entity follow;

        protected void begin () {
            startX = target.getX();
            startY = target.getY();
        }

        protected void update (float percent) {
            endX = follow.getX();
            endY = follow.getY();

            float diffX =  endX - startX;
            float diffY = endY - startY;

            target.setPosition(
                    (float)(startX + diffX * percent),
                    (float)(startY + diffY * percent)
            );
        }

        public void reset () {
            super.reset();
        }

        public void setFollow(Entity entity) {
            this.follow = entity;
        }

        public float getX () {
            return endX;
        }

        public float getY () {
            return endY;
        }

    }
