package com.dotcom.jamaatAdmin.model;

import com.dotcom.jamaatAdmin.interfaces.IMeetingAttenddesItem;

/**
 * Created by anjanik on 4/7/16.
 */

    public class AtendeeGorupHeading implements IMeetingAttenddesItem {

        private final String title;

        public AtendeeGorupHeading(String title) {
            this.title = title;
        }

        public String getTitle(){
            return title;
        }

        @Override
        public boolean isSection() {
            return true;
        }


}
