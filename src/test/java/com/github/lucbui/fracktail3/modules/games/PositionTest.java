package com.github.lucbui.fracktail3.modules.games;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    @Test
    public void isDiagonal() {
        Position one = new Position(4, 4);
        assertTrue(one.isDiagonal(new Position(5, 5)));
        assertTrue(one.isDiagonal(new Position(5, 3)));
        assertTrue(one.isDiagonal(new Position(3, 5)));
        assertTrue(one.isDiagonal(new Position(3, 3)));
    }

    @Test
    public void isDiagonal_Fails() {
        Position one = new Position(4, 4);
        assertFalse(one.isDiagonal(new Position(3, 4)));
    }

    @Test
    public void isOrthogonal() {
        Position one = new Position(4, 4);
        assertTrue(one.isOrthogonalTo(new Position(3, 4)));
        assertTrue(one.isOrthogonalTo(new Position(4, 3)));
        assertTrue(one.isOrthogonalTo(new Position(5, 4)));
        assertTrue(one.isOrthogonalTo(new Position(4, 5)));
    }

    @Test
    public void isOrthogonal_Fails() {
        Position one = new Position(4, 4);
        assertFalse(one.isOrthogonalTo(new Position(5, 5)));
    }

    @Test
    public void isAdjacent() {
        Position one = new Position(4, 4);
        assertTrue(one.isAdjacentTo(new Position(3, 3)));
        assertTrue(one.isAdjacentTo(new Position(3, 4)));
        assertTrue(one.isAdjacentTo(new Position(3, 5)));

        assertTrue(one.isAdjacentTo(new Position(4, 3)));
        assertTrue(one.isAdjacentTo(new Position(4, 5)));

        assertTrue(one.isAdjacentTo(new Position(5, 3)));
        assertTrue(one.isAdjacentTo(new Position(5, 4)));
        assertTrue(one.isAdjacentTo(new Position(5, 5)));
    }

    @Test
    public void isAdjacent_Fails() {
        Position one = new Position(4, 4);
        assertFalse(one.isAdjacentTo(new Position(2, 2)));
    }

    @Test
    public void isDistance() {
        Position one = new Position(4, 4);
        assertTrue(one.isDistanceFrom(new Position(2, 2), 2));
    }

    @Test
    public void isDistance_Fails() {
        Position one = new Position(4, 4);
        assertFalse(one.isDistanceFrom(new Position(3, 3), 2));
    }

    @Test
    public void isLeft() {
        Position one = new Position(4, 4);
        assertTrue(one.isLeftOf(new Position(6, 6)));
    }

    @Test
    public void isLeft_Fails() {
        Position one = new Position(4, 4);
        assertFalse(one.isLeftOf(new Position(2, 2)));
    }

    @Test
    public void isRight() {
        Position one = new Position(4, 4);
        assertTrue(one.isRightOf(new Position(2, 2)));
    }

    @Test
    public void isRight_Fails() {
        Position one = new Position(4, 4);
        assertFalse(one.isRightOf(new Position(6, 6)));
    }

    @Test
    public void isAbove() {
        Position one = new Position(4, 4);
        assertTrue(one.isAbove(new Position(6, 6)));
    }

    @Test
    public void isAbove_Fails() {
        Position one = new Position(4, 4);
        assertFalse(one.isAbove(new Position(2, 2)));
    }

    @Test
    public void isBelow() {
        Position one = new Position(4, 4);
        assertTrue(one.isBelow(new Position(2, 2)));
    }

    @Test
    public void isBelow_Fails() {
        Position one = new Position(4, 4);
        assertFalse(one.isBelow(new Position(6, 6)));
    }

    @Test
    public void interpolate_inside() {
        Position one = new Position(4, 4);
        Position two = new Position(6, 6);
        Position middle = one.interpolate(two, 5);
        assertEquals(5, middle.getRow());
        assertEquals(5, middle.getCol());
    }

    @Test
    public void interpolate_outside() {
        Position one = new Position(4, 4);
        Position two = new Position(6, 6);
        Position middle = one.interpolate(two, 8);
        assertEquals(8, middle.getRow());
        assertEquals(8, middle.getCol());
    }

    @Test
    public void interpolate_middle() {
        Position one = new Position(4, 4);
        Position two = new Position(6, 6);
        Position middle = one.middle(two);
        assertEquals(5, middle.getRow());
        assertEquals(5, middle.getCol());
    }
}