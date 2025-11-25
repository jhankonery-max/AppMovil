import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TextInput,
  TouchableOpacity,
  Alert,
  KeyboardAvoidingView,
  Platform
} from 'react-native';

const CrearPedidoScreen = ({ navigation }) => {
  const [formData, setFormData] = useState({
    descripcion: '',
    direccionRecogida: '',
    direccionEntrega: '',
    instruccionesEspeciales: '',
    costoEnvio: '',
    tipoPedido: 'COMIDA'
  });
  const [cargando, setCargando] = useState(false);

  const tiposPedido = [
    { id: 'COMIDA', nombre: '🍕 Comida', icon: '🍕' },
    { id: 'FARMACIA', nombre: '💊 Farmacia', icon: '💊' },
    { id: 'SUPERMERCADO', nombre: '🛒 Supermercado', icon: '🛒' },
    { id: 'MENSAJERIA', nombre: '📦 Mensajería', icon: '📦' },
    { id: 'OTROS', nombre: '📝 Otros', icon: '📝' }
  ];

  const validarFormulario = () => {
    if (!formData.descripcion.trim()) {
      Alert.alert('Error', 'La descripción del pedido es obligatoria');
      return false;
    }

    if (!formData.direccionRecogida.trim()) {
      Alert.alert('Error', 'La dirección de recogida es obligatoria');
      return false;
    }

    if (!formData.direccionEntrega.trim()) {
      Alert.alert('Error', 'La dirección de entrega es obligatoria');
      return false;
    }

    if (!formData.costoEnvio || parseFloat(formData.costoEnvio) <= 0) {
      Alert.alert('Error', 'El costo de envío debe ser mayor a 0');
      return false;
    }

    return true;
  };

  const handleCrearPedido = async () => {
    if (!validarFormulario()) return;

    setCargando(true);
    try {
      // Simular creación de pedido
      setTimeout(() => {
        Alert.alert(
          '¡Pedido Creado!',
          'Tu pedido ha sido creado exitosamente. Un repartidor será asignado pronto.',
          [
            {
              text: 'Ver Pedidos',
              onPress: () => navigation.navigate('Home')
            },
            {
              text: 'Seguir Creando',
              style: 'cancel'
            }
          ]
        );
        setFormData({
          descripcion: '',
          direccionRecogida: '',
          direccionEntrega: '',
          instruccionesEspeciales: '',
          costoEnvio: '',
          tipoPedido: 'COMIDA'
        });
        setCargando(false);
      }, 1000);
    } catch (error) {
      Alert.alert('Error', 'Error al crear el pedido');
      setCargando(false);
    }
  };

  const usarUbicacionActual = (tipo) => {
    const ubicacionesEjemplo = {
      recogida: 'Centro Comercial Andino, Bogotá',
      entrega: 'Mi ubicación actual'
    };

    if (tipo === 'recogida') {
      setFormData({...formData, direccionRecogida: ubicacionesEjemplo.recogida});
    } else {
      setFormData({...formData, direccionEntrega: ubicacionesEjemplo.entrega});
    }
  };

  return (
    <KeyboardAvoidingView style={styles.container} behavior={Platform.OS === 'ios' ? 'padding' : 'height'}>
      <ScrollView>
        <View style={styles.header}>
          <Text style={styles.titulo}>Nuevo Pedido</Text>
          <Text style={styles.subtitulo}>Completa la información de tu envío</Text>
        </View>

        <View style={styles.formContainer}>
          <View style={styles.seccion}>
            <Text style={styles.label}>Tipo de Pedido *</Text>
            <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.tiposContainer}>
              {tiposPedido.map((tipo) => (
                <TouchableOpacity
                  key={tipo.id}
                  style={[styles.tipoOption, formData.tipoPedido === tipo.id && styles.tipoOptionSelected]}
                  onPress={() => setFormData({...formData, tipoPedido: tipo.id})}
                >
                  <Text style={styles.tipoIcon}>{tipo.icon}</Text>
                  <Text style={[styles.tipoText, formData.tipoPedido === tipo.id && styles.tipoTextSelected]}>
                    {tipo.nombre}
                  </Text>
                </TouchableOpacity>
              ))}
            </ScrollView>
          </View>

          <View style={styles.campo}>
            <Text style={styles.label}>¿Qué quieres enviar? *</Text>
            <TextInput
              style={styles.input}
              placeholder="Ej: Comida de McDonald's, Medicamentos de farmacia, etc."
              value={formData.descripcion}
              onChangeText={(text) => setFormData({...formData, descripcion: text})}
              multiline
            />
          </View>

          <View style={styles.campo}>
            <View style={styles.campoHeader}>
              <Text style={styles.label}>Dirección de Recogida *</Text>
              <TouchableOpacity onPress={() => usarUbicacionActual('recogida')}>
                <Text style={styles.ubicacionLink}>Usar ubicación común</Text>
              </TouchableOpacity>
            </View>
            <TextInput
              style={styles.input}
              placeholder="¿Dónde deben recoger tu pedido?"
              value={formData.direccionRecogida}
              onChangeText={(text) => setFormData({...formData, direccionRecogida: text})}
              multiline
            />
          </View>

          <View style={styles.campo}>
            <View style={styles.campoHeader}>
              <Text style={styles.label}>Dirección de Entrega *</Text>
              <TouchableOpacity onPress={() => usarUbicacionActual('entrega')}>
                <Text style={styles.ubicacionLink}>Mi ubicación actual</Text>
              </TouchableOpacity>
            </View>
            <TextInput
              style={styles.input}
              placeholder="¿A dónde debe llegar tu pedido?"
              value={formData.direccionEntrega}
              onChangeText={(text) => setFormData({...formData, direccionEntrega: text})}
              multiline
            />
          </View>

          <View style={styles.campo}>
            <Text style={styles.label}>Instrucciones Especiales</Text>
            <TextInput
              style={[styles.input, styles.textArea]}
              placeholder="Ej: Llamar antes de llegar, código de seguridad, punto de referencia..."
              value={formData.instruccionesEspeciales}
              onChangeText={(text) => setFormData({...formData, instruccionesEspeciales: text})}
              multiline
              numberOfLines={3}
              textAlignVertical="top"
            />
          </View>

          <View style={styles.campo}>
            <Text style={styles.label}>Costo de Envío (COP) *</Text>
            <View style={styles.costoContainer}>
              <Text style={styles.costoSimbolo}>$</Text>
              <TextInput
                style={[styles.input, styles.costoInput]}
                placeholder="0"
                keyboardType="numeric"
                value={formData.costoEnvio}
                onChangeText={(text) => setFormData({...formData, costoEnvio: text})}
              />
            </View>
            <Text style={styles.ayudaCosto}>💡 El costo sugerido para distancias cortas es $5,000 - $10,000 COP</Text>
          </View>

          <View style={styles.resumenContainer}>
            <Text style={styles.resumenTitulo}>Resumen del Pedido</Text>
            <View style={styles.resumenItem}>
              <Text style={styles.resumenLabel}>Tipo:</Text>
              <Text style={styles.resumenValor}>{tiposPedido.find(t => t.id === formData.tipoPedido)?.nombre}</Text>
            </View>
            <View style={styles.resumenItem}>
              <Text style={styles.resumenLabel}>Costo envío:</Text>
              <Text style={styles.resumenValor}>${formData.costoEnvio ? parseFloat(formData.costoEnvio).toLocaleString() : '0'} COP</Text>
            </View>
            <View style={styles.resumenTotal}>
              <Text style={styles.resumenTotalLabel}>Total aproximado:</Text>
              <Text style={styles.resumenTotalValor}>${formData.costoEnvio ? parseFloat(formData.costoEnvio).toLocaleString() : '0'} COP</Text>
            </View>
          </View>

          <TouchableOpacity style={[styles.botonCrear, cargando && styles.botonDeshabilitado]} onPress={handleCrearPedido} disabled={cargando}>
            <Text style={styles.botonCrearTexto}>{cargando ? 'Creando Pedido...' : '📦 Crear Pedido'}</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
  },
  header: {
    backgroundColor: 'white',
    padding: 20,
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
  },
  titulo: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 5,
  },
  subtitulo: {
    fontSize: 14,
    color: '#666',
  },
  formContainer: {
    padding: 20,
  },
  seccion: {
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 10,
    color: '#333',
  },
  tiposContainer: {
    flexDirection: 'row',
  },
  tipoOption: {
    alignItems: 'center',
    padding: 12,
    backgroundColor: 'white',
    borderRadius: 10,
    marginRight: 10,
    minWidth: 80,
    borderWidth: 2,
    borderColor: 'transparent',
  },
  tipoOptionSelected: {
    borderColor: '#007AFF',
    backgroundColor: '#e3f2fd',
  },
  tipoIcon: {
    fontSize: 20,
    marginBottom: 5,
  },
  tipoText: {
    fontSize: 12,
    textAlign: 'center',
    color: '#666',
  },
  tipoTextSelected: {
    color: '#007AFF',
    fontWeight: '600',
  },
  campo: {
    marginBottom: 20,
  },
  campoHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  ubicacionLink: {
    color: '#007AFF',
    fontSize: 14,
    fontWeight: '500',
  },
  input: {
    backgroundColor: 'white',
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 10,
    padding: 15,
    fontSize: 16,
  },
  textArea: {
    minHeight: 80,
    textAlignVertical: 'top',
  },
  costoContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  costoSimbolo: {
    fontSize: 18,
    fontWeight: 'bold',
    marginRight: 10,
    color: '#333',
  },
  costoInput: {
    flex: 1,
  },
  ayudaCosto: {
    fontSize: 12,
    color: '#666',
    marginTop: 5,
    fontStyle: 'italic',
  },
  resumenContainer: {
    backgroundColor: '#e8f5e8',
    padding: 15,
    borderRadius: 10,
    marginBottom: 20,
  },
  resumenTitulo: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 10,
    color: '#2e7d32',
  },
  resumenItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 5,
  },
  resumenLabel: {
    fontSize: 14,
    color: '#555',
  },
  resumenValor: {
    fontSize: 14,
    fontWeight: '500',
    color: '#333',
  },
  resumenTotal: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 10,
    paddingTop: 10,
    borderTopWidth: 1,
    borderTopColor: '#c8e6c9',
  },
  resumenTotalLabel: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#2e7d32',
  },
  resumenTotalValor: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#2e7d32',
  },
  botonCrear: {
    backgroundColor: '#28a745',
    padding: 18,
    borderRadius: 10,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  botonDeshabilitado: {
    backgroundColor: '#6c757d',
  },
  botonCrearTexto: {
    color: 'white',
    fontSize: 18,
    fontWeight: 'bold',
  },
});

export default CrearPedidoScreen;