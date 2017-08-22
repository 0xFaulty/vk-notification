package com.defaulty.notivk.gui.components;

import org.junit.Test;

import static org.junit.Assert.*;

public class GroupAddTest {

    @Test
    public void detectGroupName() throws Exception {
        GroupAdd groupAdd = new GroupAdd("Добавить группу", "Введите название:", "Добавить");
        assertEquals(groupAdd.detectGroupName(""),"");
        assertEquals(groupAdd.detectGroupName("https://vk.com/pikabu"),"pikabu");
        assertEquals(groupAdd.detectGroupName("vk.com/pikabu"),"pikabu");
        assertEquals(groupAdd.detectGroupName("a11test99#11!/pikabu"),"pikabu");
        assertEquals(groupAdd.detectGroupName("pikabu"),"pikabu");
        assertEquals(groupAdd.detectGroupName("https://vk.com/club149081239"),"149081239");
        assertEquals(groupAdd.detectGroupName("vk.com/club149081239"),"149081239");
        assertEquals(groupAdd.detectGroupName("cool$%^&1/club149081239"),"149081239");
    }

}