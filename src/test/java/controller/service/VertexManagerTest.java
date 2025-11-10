package controller.service;

import model.AlgorithmConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VertexManagerTest {

    @Test
    @DisplayName("Добавление вершин ограничено MAX_TRIANGLE_POINTS")
    void addVertex_ShouldRespectLimit() {
        VertexManager vm = new VertexManager();
        assertTrue(vm.addVertex(10, 10));
        assertTrue(vm.addVertex(20, 20));
        assertTrue(vm.addVertex(30, 30));
        assertFalse(vm.addVertex(40, 40));
        assertEquals(AlgorithmConstants.MAX_TRIANGLE_POINTS, vm.getVertices().size());
        assertTrue(vm.hasEnoughVertices());
    }

    @Test
    @DisplayName("Удаление ближайшей вершины в радиусе должно работать")
    void removeNearestVertex_ShouldWorkWithinRadius() {
        VertexManager vm = new VertexManager();
        vm.addVertex(10, 10);
        vm.addVertex(100, 100);
        assertTrue(vm.removeNearestVertex(12, 12, AlgorithmConstants.POINT_DELETE_RADIUS));
        assertEquals(1, vm.getVertices().size());
        assertFalse(vm.removeNearestVertex(300, 300, AlgorithmConstants.POINT_DELETE_RADIUS));
    }
}
