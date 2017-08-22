package com.defaulty.notivk.backend;

import java.util.ArrayList;
import java.util.List;

/**
 * The class {@code GroupData} содержит данные о группе.
 * groupId - идентификационный номер группы,
 * tags - список тегов указанных пользователем для фильтрации постов с группы,
 * enableTags - флаг применения фильтра по тегам.
 */
class GroupData {

    private String groupId = "";
    private List<String> tags = new ArrayList<>();
    private Boolean enableTags = false;

    String getGroupId() {
        return groupId;
    }

    void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    List<String> getTags() {
        return tags;
    }

    void setTags(List<String> tags) {
        this.tags = tags;
    }

    Boolean isEnableTags() {
        return enableTags;
    }

    void setEnableTags(boolean enableTags) {
        this.enableTags = enableTags;
    }
}
