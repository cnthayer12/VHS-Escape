package com.model;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TestPlayers {

    private Players players;

    @Before
    public void setUp() {
        players = Players.getInstance();
        players.logout();
        players.setPlayers(new java.util.ArrayList<Player>());
    }

    @After
    public void tearDown() {
        players.logout();
        players.setPlayers(new java.util.ArrayList<Player>());
    }

    // --- Account Creation ---
    @Test
    public void testCreateAccountSuccess() {
        players.createAccount("test_user", "testpass");
        assertEquals("Players list should contain one player after creation", 1, Players.getPlayers().size());
        assertEquals("New player's username should match input", "test_user", Players.getPlayers().get(0).getDisplayName());
    }

    @Test
    public void testCreateAccountDuplicateUsername() {
        players.createAccount("duplicate_user", "abc123");
        int beforeSize = Players.getPlayers().size();

        players.createAccount("duplicate_user", "differentpass");
        int afterSize = Players.getPlayers().size();

        assertEquals("Duplicate usernames should not create new accounts", beforeSize, afterSize);
    }

    @Test
    public void testCreateAccountNoPassword() {
        players.createAccount("nopass_user", "");
        assertTrue("Blank passwords should not create a player", Players.getPlayers().isEmpty());
    }

    @Test
    public void testCreateAccountBlankUsername() {
        players.createAccount("", "password");
        assertTrue("Blank usernames should not create a player", Players.getPlayers().isEmpty());
    }

    @Test
    public void testCreateAccountNullUsername() {
        players.createAccount(null, "password");
        assertTrue("Null username should not create a player", Players.getPlayers().isEmpty());
    }

    @Test
    public void testCreateAccountNullPassword() {
        players.createAccount("nullpass_user", null);
        assertTrue("Null password should not create a player", Players.getPlayers().isEmpty());
    }

    @Test
    public void testCreateAccountBothNull() {
        players.createAccount(null, null);
        assertTrue("Null username and password should not create a player", Players.getPlayers().isEmpty());
    }

    @Test
    public void testPlayersListUpdatesAfterCreateAccount() {
        int beforeSize = Players.getPlayers().size();
        players.createAccount("listcheck_user", "password");
        assertTrue("Players list should update after an account is created", beforeSize < Players.getPlayers().size());
    }

    // --- Login / Logout ---
    @Test
    public void testLoginSuccess() {
        players.createAccount("login_test", "1234");
        players.logout();

        players.login("login_test", "1234");
        assertNotNull("Current player should be set after valid login", Players.getCurrentPlayer());
        assertEquals("login_test", Players.getCurrentPlayer().getDisplayName());
    }

    @Test
    public void testLoginInvalidUsername() {
        players.login("fake_user", "password");
        assertNull("Current player should remain null for invalid username", Players.getCurrentPlayer());
    }

    @Test
    public void testLoginInvalidPassword() {
        players.createAccount("wrongpass_user", "correctpass");
        players.logout();
        players.login("wrongpass_user", "incorrectpass");

        assertNull("Current player should be null for invalid password", Players.getCurrentPlayer());
    }

    @Test
    public void testLoginCaseSensitivity() {
        players.createAccount("caseSensitivity", "password");
        players.logout();
        players.login("casesensitivity", "password");

        assertNull("Usernames are case sensitive, so should not log in for incorrect capitalization", Players.getCurrentPlayer());
    }

    @Test
    public void testLogoutClearsCurrentPlayer() {
        players.createAccount("logoutClear", "password");
        assertNotNull("Auto-login when creating an account", Players.getCurrentPlayer());
        players.logout();

        assertNull("Logging out should set currentPlayer to null", Players.getCurrentPlayer());
    }

    @Test
    public void testLogoutWhenNoOneLoggedIn() {
        players.logout();
        assertNull("Logging out while not logged in shouldn't change anything", Players.getCurrentPlayer());
    }

    // --- Management / Utility ---
    @Test
    public void testSetPlayersReplacesList() {
        ArrayList<Player> newList = new ArrayList<>();
        Player player = new Player("new", null, "pass");
        newList.add(player);
        players.setPlayers(newList);

        assertTrue("setPlayer should change the ArrayList", newList.equals(Players.getPlayers()));
    }

    @Test
    public void testRemovePlayerValidId() {
        players.createAccount("removePlayer", "password");
        Player player = Players.getPlayers().get(0);
        players.removePlayer(player.getId());

        assertFalse("Removing a valid ID should remove that player from the list", Players.getPlayers().contains(player));
    }

    @Test
    public void testRemovePlayerInvalidId() {
        players.createAccount("removePlayer", "password");
        Player player = Players.getPlayers().get(0);
        players.removePlayer(UUID.randomUUID());

        assertTrue("Removing an invalid ID should not remove any player from the list", Players.getPlayers().contains(player));
    }

    @Test
    public void testGetInstanceAlwaysSameObject() {
        Players first = Players.getInstance();
        Players second = Players.getInstance();
        assertNotNull("Players.getInstance() should never return null", first);
        assertSame("Players.getInstance() should always return the same instance", first, second);
    }

    @Test
    public void testGetCurrentPlayerBeforeLogin() {
        assertNull("Before logging in, getCurrentPlayer should return null", Players.getCurrentPlayer());
    }
}
