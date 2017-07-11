package com.defaulty.notivk.backend;

import java.util.ArrayList;
import java.util.List;

class GroupData {

    private String groupId = "";
    private String groupNameId = "";
    private List<String> tags = new ArrayList<>();
    private Boolean enableTags = false;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupNameId() {
        return groupNameId;
    }

    public void setGroupNameId(String groupNameId) {
        this.groupNameId = groupNameId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean isEnableTags() {
        return enableTags;
    }

    public void setEnableTags(boolean enableTags) {
        this.enableTags = enableTags;
    }
}
