package com.defaulty.notivk.backend;

import org.junit.*;

import java.io.File;
import java.util.ArrayList;

import static junit.framework.TestCase.*;

public class SettingsWrapperTest {

    private static SettingsWrapper settings = SettingsWrapper.getInstance();

    @BeforeClass
    public static void setUp() throws Exception {
        File file = new File("test_settings.xml");
        System.out.println(file.delete());
        settings.setSettingsFileName("test_settings.xml");
        settings.loadSettings();
    }

    @Test
    public void GroupIdListTest() throws Exception {
        settings.addGroupId("1");
        settings.removeGroupId("1");
        settings.addGroupId("1");
        settings.addGroupId("2");
        settings.setGroupTags("1", new ArrayList<>());
        settings.setGroupCheckState("1", true);

        settings.save();
        settings.loadSettings();

        assertFalse(settings.getGroupIdList().isEmpty());
        assertFalse(settings.getGroupDataList().isEmpty());
        assertTrue(settings.getGroupCheckState("1"));
    }


    @Test
    public void UserDataTest() throws Exception {
        settings.setUserData(new UserData("1", "test_token"));
        settings.setNotifyState(true);

        settings.save();
        settings.loadSettings();

        settings.setUserName("User Name");
        assertEquals(settings.getUserName(), "User Name");
        assertEquals(settings.getUserId(), "1");
        assertNotNull(settings.getUserData());
        assertTrue(settings.getNotifyState());
    }

}