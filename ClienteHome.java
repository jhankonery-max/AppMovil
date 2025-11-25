import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  RefreshControl,
  Alert
} from 'react-native';

const ClienteHomeScreen = ({ navigation }) => {
  const [usuario, setUsuario] = useState(null);
  const [pedidos, setPedidos] = useState([]);
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    cargarDatosUsuario();
    cargarPedidos();
  }, []);

  const cargarDatosUsuario = async () => {
    setUsuario({
      nombre: 'Ana García',
      tipo: 'CLIENTE',
      email: 'ana@email.com'
    });
  };

  const cargarPedidos = async () => {
    try {
      const pedidosEjemplo = [
        {
          id: 1,
          descripcion: 'Comida rápida - McDonald\'s',
          direccionEntrega: 'Calle 123 #45-67, Bogotá',
          estado: 'EN_CAMINO',
          total: 45000,
          fecha: '2024-01-15 14:30',
          repartidor: {
            nombre: 'Carlos Rodríguez',
            vehiculo: 'Moto',
            calificacion: 4.8
          }
        },
        {
          id: 2,
          descripcion: 'Farmacia - Droguería',
          direccionEntrega: 'Carrera 78 #12-34, Bogotá',
          estado: 'ENTREGADO',
          total: 32000,
          fecha: '2024-01-14 11:20'
        }
      ];
      setPedidos(pedidosEjemplo);
    } catch (error) {
      Alert.alert('Error', 'Error al cargar pedidos');
    }
  };

  const onRefresh = () => {
    setRefreshing(true);
    cargarPedidos();
    setTimeout(() => setRefreshing(false), 1000);
  };

  const navegarACrearPedido = () => {
    navigation.navigate('CrearPedido');
  };

  const verDetallePedido = (pedido) => {
    navigation.navigate('DetallePedido', { pedido });
  };

  const getColorEstado = (estado) => {
    switch (estado) {
      case 'PENDIENTE': return '#ffc107';
      case 'ASIGNADO': return '#17a2b8';
      case 'EN_CAMINO': return '#007bff';
      case 'ENTREGADO': return '#28a745';
      case 'CANCELADO': return '#dc3545';
      default: return '#6c757d';
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <View>
          <Text style={styles.saludo}>¡Hola, {usuario?.nombre || 'Cliente'}!</Text>
          <Text style={styles.subtitulo}>¿Qué quieres pedir hoy?</Text>
        </View>
        <TouchableOpacity onPress={() => navigation.navigate('Perfil')}>
          <View style={styles.avatar}>
            <Text style={styles.avatarTexto}>
              {usuario?.nombre?.charAt(0) || 'C'}
            </Text>
          </View>
        </TouchableOpacity>
      </View>

      <ScrollView refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} />}>
        <TouchableOpacity style={styles.botonCrearPedido} onPress={navegarACrearPedido}>
          <Text style={styles.botonCrearPedidoTexto}>+ Crear Nuevo Pedido</Text>
        </TouchableOpacity>

        <View style={styles.seccion}>
          <Text style={styles.tituloSeccion}>Pedidos en Proceso</Text>
          {pedidos.filter(p => p.estado !== 'ENTREGADO').map((pedido) => (
            <TouchableOpacity key={pedido.id} style={styles.tarjetaPedido} onPress={() => verDetallePedido(pedido)}>
              <View style={styles.pedidoHeader}>
                <Text style={styles.pedidoDescripcion}>{pedido.descripcion}</Text>
                <Text style={styles.pedidoTotal}>${pedido.total.toLocaleString()}</Text>
              </View>
              <Text style={styles.pedidoDireccion}>📍 {pedido.direccionEntrega}</Text>
              <View style={styles.pedidoFooter}>
                <View style={[styles.estado, { backgroundColor: getColorEstado(pedido.estado) }]}>
                  <Text style={styles.estadoTexto}>{pedido.estado}</Text>
                </View>
                {pedido.repartidor && (
                  <Text style={styles.repartidorInfo}>🚗 {pedido.repartidor.nombre} ({pedido.repartidor.vehiculo})</Text>
                )}
              </View>
            </TouchableOpacity>
          ))}
        </View>

        <View style={styles.seccion}>
          <Text style={styles.tituloSeccion}>Historial de Pedidos</Text>
          {pedidos.filter(p => p.estado === 'ENTREGADO').map((pedido) => (
            <TouchableOpacity key={pedido.id} style={styles.tarjetaPedido} onPress={() => verDetallePedido(pedido)}>
              <View style={styles.pedidoHeader}>
                <Text style={styles.pedidoDescripcion}>{pedido.descripcion}</Text>
                <Text style={styles.pedidoTotal}>${pedido.total.toLocaleString()}</Text>
              </View>
              <Text style={styles.pedidoDireccion}>📍 {pedido.direccionEntrega}</Text>
              <Text style={styles.pedidoFecha}>📅 {pedido.fecha}</Text>
            </TouchableOpacity>
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
  subtitulo: {
    fontSize: 14,
    color: '#666',
    marginTop: 4,
  },
  avatar: {
    width: 50,
    height: 50,
    borderRadius: 25,
    backgroundColor: '#007AFF',
    justifyContent: 'center',
    alignItems: 'center',
  },
  avatarTexto: {
    color: 'white',
    fontSize: 20,
    fontWeight: 'bold',
  },
  botonCrearPedido: {
    backgroundColor: '#28a745',
    margin: 15,
    padding: 15,
    borderRadius: 10,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  botonCrearPedidoTexto: {
    color: 'white',
    fontSize: 18,
    fontWeight: 'bold',
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
  tarjetaPedido: {
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
    marginBottom: 8,
  },
  pedidoDescripcion: {
    fontSize: 16,
    fontWeight: 'bold',
    flex: 1,
    color: '#333',
  },
  pedidoTotal: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#28a745',
  },
  pedidoDireccion: {
    fontSize: 14,
    color: '#666',
    marginBottom: 5,
  },
  pedidoFecha: {
    fontSize: 12,
    color: '#888',
  },
  pedidoFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginTop: 10,
  },
  estado: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
  },
  estadoTexto: {
    fontSize: 10,
    fontWeight: 'bold',
    color: 'white',
  },
  repartidorInfo: {
    fontSize: 12,
    color: '#666',
  },
});

export default ClienteHomeScreen;