import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Switch,
  Alert,
  RefreshControl
} from 'react-native';

const RepartidorHomeScreen = ({ navigation }) => {
  const [repartidor, setRepartidor] = useState(null);
  const [pedidosDisponibles, setPedidosDisponibles] = useState([]);
  const [pedidosActivos, setPedidosActivos] = useState([]);
  const [disponible, setDisponible] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    cargarDatosRepartidor();
    cargarPedidos();
  }, []);

  const cargarDatosRepartidor = async () => {
    setRepartidor({
      nombre: 'Carlos Rodríguez',
      tipo: 'REPARTIDOR',
      vehiculo: 'Moto',
      placa: 'ABC123',
      calificacion: 4.8,
      entregasCompletadas: 47
    });
  };

  const cargarPedidos = async () => {
    try {
      const pedidosDisponiblesEjemplo = [
        {
          id: 1,
          descripcion: 'Comida rápida - McDonald\'s',
          direccionRecogida: 'Centro Comercial Andino',
          direccionEntrega: 'Calle 123 #45-67, Bogotá',
          distancia: '2.3 km',
          costoEnvio: 8000,
          total: 45000,
          tiempoEstimado: '15-20 min'
        },
        {
          id: 2,
          descripcion: 'Farmacia - Medicamentos',
          direccionRecogida: 'Droguería Colsubsidio',
          direccionEntrega: 'Carrera 78 #12-34, Bogotá',
          distancia: '3.1 km',
          costoEnvio: 9500,
          total: 32000,
          tiempoEstimado: '20-25 min'
        }
      ];

      const pedidosActivosEjemplo = [
        {
          id: 3,
          descripcion: 'Supermercado - Mercado',
          direccionRecogida: 'Éxito Calle 80',
          direccionEntrega: 'Calle 85 #23-45, Bogotá',
          estado: 'EN_CAMINO',
          cliente: {
            nombre: 'María López',
            telefono: '+57 300 987 6543'
          }
        }
      ];

      setPedidosDisponibles(pedidosDisponiblesEjemplo);
      setPedidosActivos(pedidosActivosEjemplo);
    } catch (error) {
      Alert.alert('Error', 'Error al cargar pedidos');
    }
  };

  const onRefresh = () => {
    setRefreshing(true);
    cargarPedidos();
    setTimeout(() => setRefreshing(false), 1000);
  };

  const toggleDisponibilidad = () => {
    setDisponible(!disponible);
  };

  const aceptarPedido = (pedido) => {
    Alert.alert(
      'Aceptar Pedido',
      `¿Aceptar el pedido: ${pedido.descripcion}?`,
      [
        { text: 'Cancelar', style: 'cancel' },
        { 
          text: 'Aceptar', 
          onPress: () => {
            setPedidosDisponibles(pedidosDisponibles.filter(p => p.id !== pedido.id));
            setPedidosActivos([...pedidosActivos, { ...pedido, estado: 'ASIGNADO' }]);
            Alert.alert('Éxito', 'Pedido aceptado correctamente');
          }
        }
      ]
    );
  };

  const completarPedido = (pedido) => {
    Alert.alert(
      'Completar Pedido',
      `¿Marcar como entregado: ${pedido.descripcion}?`,
      [
        { text: 'Cancelar', style: 'cancel' },
        { 
          text: 'Entregado', 
          onPress: () => {
            setPedidosActivos(pedidosActivos.filter(p => p.id !== pedido.id));
            Alert.alert('Éxito', 'Pedido marcado como entregado');
          }
        }
      ]
    );
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <View>
          <Text style={styles.saludo}>Hola, {repartidor?.nombre}</Text>
          <Text style={styles.estadisticas}>🏆 {repartidor?.entregasCompletadas} entregas • ⭐ {repartidor?.calificacion}</Text>
        </View>
        <View style={styles.estadoContainer}>
          <Text style={styles.estadoTexto}>{disponible ? '🟢 Disponible' : '🔴 Ocupado'}</Text>
          <Switch
            value={disponible}
            onValueChange={toggleDisponibilidad}
            trackColor={{ false: '#767577', true: '#81b0ff' }}
            thumbColor={disponible ? '#007AFF' : '#f4f3f4'}
          />
        </View>
      </View>

      <ScrollView refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} />}>
        {pedidosActivos.length > 0 && (
          <View style={styles.seccion}>
            <Text style={styles.tituloSeccion}>Pedidos Activos</Text>
            {pedidosActivos.map((pedido) => (
              <View key={pedido.id} style={styles.tarjetaPedidoActivo}>
                <View style={styles.pedidoHeader}>
                  <Text style={styles.pedidoDescripcion}>{pedido.descripcion}</Text>
                  <Text style={styles.estadoActivo}>🟡 {pedido.estado}</Text>
                </View>
                <View style={styles.ruta}>
                  <Text style={styles.rutaPunto}>📍 Recoger: {pedido.direccionRecogida}</Text>
                  <Text style={styles.rutaPunto}>🎯 Entregar: {pedido.direccionEntrega}</Text>
                </View>
                {pedido.cliente && (
                  <Text style={styles.infoCliente}>👤 {pedido.cliente.nombre} • 📞 {pedido.cliente.telefono}</Text>
                )}
                <TouchableOpacity style={styles.botonCompletar} onPress={() => completarPedido(pedido)}>
                  <Text style={styles.botonCompletarTexto}>✅ Marcar como Entregado</Text>
                </TouchableOpacity>
              </View>
            ))}
          </View>
        )}

        <View style={styles.seccion}>
          <Text style={styles.tituloSeccion}>Pedidos Disponibles {!disponible && '(No disponible)'}</Text>
          {pedidosDisponibles.map((pedido) => (
            <View key={pedido.id} style={styles.tarjetaPedidoDisponible}>
              <View style={styles.pedidoHeader}>
                <Text style={styles.pedidoDescripcion}>{pedido.descripcion}</Text>
                <Text style={styles.pedidoCosto}>${pedido.costoEnvio.toLocaleString()}</Text>
              </View>
              <View style={styles.ruta}>
                <Text style={styles.rutaPunto}>📍 {pedido.direccionRecogida}</Text>
                <Text style={styles.rutaPunto}>🎯 {pedido.direccionEntrega}</Text>
              </View>
              <View style={styles.pedidoInfo}>
                <Text style={styles.pedidoDetalle}>📏 {pedido.distancia}</Text>
                <Text style={styles.pedidoDetalle}>⏱️ {pedido.tiempoEstimado}</Text>
                <Text style={styles.pedidoDetalle}>💰 Total: ${pedido.total.toLocaleString()}</Text>
              </View>
              <TouchableOpacity 
                style={[styles.botonAceptar, !disponible && styles.botonDeshabilitado]}
                onPress={() => aceptarPedido(pedido)}
                disabled={!disponible}
              >
                <Text style={styles.botonAceptarTexto}>{disponible ? '✅ Aceptar Pedido' : 'No disponible'}</Text>
              </TouchableOpacity>
            </View>
          ))}
        </View>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 20,
    backgroundColor: 'white',
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  saludo: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#333',
  },
  estadisticas: {
    fontSize: 14,
    color: '#666',
    marginTop: 4,
  },
  estadoContainer: {
    alignItems: 'center',
  },
  estadoTexto: {
    fontSize: 12,
    marginBottom: 5,
    fontWeight: '500',
  },
  seccion: {
    padding: 15,
  },
  tituloSeccion: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 15,
    color: '#333',
  },
  tarjetaPedidoActivo: {
    backgroundColor: '#fff3cd',
    padding: 15,
    borderRadius: 10,
    marginBottom: 10,
    borderLeftWidth: 4,
    borderLeftColor: '#ffc107',
  },
  tarjetaPedidoDisponible: {
    backgroundColor: 'white',
    padding: 15,
    borderRadius: 10,
    marginBottom: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 3,
    elevation: 2,
  },
  pedidoHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: 10,
  },
  pedidoDescripcion: {
    fontSize: 16,
    fontWeight: 'bold',
    flex: 1,
    color: '#333',
  },
  pedidoCosto: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#28a745',
  },
  estadoActivo: {
    fontSize: 12,
    fontWeight: 'bold',
    color: '#856404',
  },
  ruta: {
    marginBottom: 10,
  },
  rutaPunto: {
    fontSize: 14,
    color: '#666',
    marginBottom: 3,
  },
  infoCliente: {
    fontSize: 14,
    color: '#333',
    marginBottom: 10,
    fontWeight: '500',
  },
  pedidoInfo: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 10,
  },
  pedidoDetalle: {
    fontSize: 12,
    color: '#666',
  },
  botonAceptar: {
    backgroundColor: '#28a745',
    padding: 12,
    borderRadius: 8,
    alignItems: 'center',
  },
  botonDeshabilitado: {
    backgroundColor: '#6c757d',
  },
  botonAceptarTexto: {
    color: 'white',
    fontWeight: 'bold',
  },
  botonCompletar: {
    backgroundColor: '#007AFF',
    padding: 12,
    borderRadius: 8,
    alignItems: 'center',
  },
  botonCompletarTexto: {
    color: 'white',
    fontWeight: 'bold',
  },
});

export default RepartidorHomeScreen;