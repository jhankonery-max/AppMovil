import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TextInput,
  TouchableOpacity,
  Alert
} from 'react-native';

const PerfilScreen = ({ navigation }) => {
  const [perfil, setPerfil] = useState({
    nombreCompleto: '',
    email: '',
    telefono: '',
    direccion: '',
    descripcion: '',
    tipo: 'CLIENTE'
  });
  const [editando, setEditando] = useState(false);
  const [cargando, setCargando] = useState(false);

  useEffect(() => {
    cargarPerfil();
  }, []);

  const cargarPerfil = async () => {
    setPerfil({
      nombreCompleto: 'Juan Pérez',
      email: 'juan@email.com',
      telefono: '+57 300 123 4567',
      direccion: 'Calle 123 #45-67, Bogotá',
      descripcion: 'Cliente buscando servicios de delivery confiables',
      tipo: 'CLIENTE'
    });
  };

  const guardarPerfil = async () => {
    if (!perfil.nombreCompleto.trim()) {
      Alert.alert('Error', 'El nombre completo es obligatorio');
      return;
    }

    setCargando(true);
    try {
      setTimeout(() => {
        Alert.alert('Éxito', 'Perfil actualizado correctamente');
        setEditando(false);
        setCargando(false);
      }, 1000);
    } catch (error) {
      Alert.alert('Error', 'Error al guardar perfil');
      setCargando(false);
    }
  };

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.titulo}>Mi Perfil</Text>
        <TouchableOpacity onPress={() => setEditando(!editando)} style={styles.botonEditar}>
          <Text style={styles.botonEditarTexto}>{editando ? 'Cancelar' : 'Editar'}</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.formContainer}>
        <View style={styles.seccion}>
          <Text style={styles.subtitulo}>Información Básica</Text>
          <View style={styles.campo}>
            <Text style={styles.label}>Nombre Completo *</Text>
            <TextInput
              style={[styles.input, !editando && styles.inputDeshabilitado]}
              value={perfil.nombreCompleto}
              onChangeText={(text) => setPerfil({...perfil, nombreCompleto: text})}
              editable={editando}
              placeholder="Ingresa tu nombre completo"
            />
          </View>
          <View style={styles.campo}>
            <Text style={styles.label}>Email *</Text>
            <TextInput
              style={[styles.input, styles.inputDeshabilitado]}
              value={perfil.email}
              editable={false}
              keyboardType="email-address"
            />
          </View>
          <View style={styles.campo}>
            <Text style={styles.label}>Teléfono</Text>
            <TextInput
              style={[styles.input, !editando && styles.inputDeshabilitado]}
              value={perfil.telefono}
              onChangeText={(text) => setPerfil({...perfil, telefono: text})}
              editable={editando}
              keyboardType="phone-pad"
              placeholder="+57 300 123 4567"
            />
          </View>
        </View>

        <View style={styles.seccion}>
          <Text style={styles.subtitulo}>{perfil.tipo === 'CLIENTE' ? 'Mi Dirección' : 'Mi Información'}</Text>
          <View style={styles.campo}>
            <Text style={styles.label}>Dirección</Text>
            <TextInput
              style={[styles.input, !editando && styles.inputDeshabilitado]}
              value={perfil.direccion}
              onChangeText={(text) => setPerfil({...perfil, direccion: text})}
              editable={editando}
              placeholder="Ingresa tu dirección"
              multiline
            />
          </View>
          <View style={styles.campo}>
            <Text style={styles.label}>{perfil.tipo === 'CLIENTE' ? 'Preferencias' : 'Descripción'}</Text>
            <TextInput
              style={[styles.textArea, !editando && styles.inputDeshabilitado]}
              value={perfil.descripcion}
              onChangeText={(text) => setPerfil({...perfil, descripcion: text})}
              editable={editando}
              placeholder={perfil.tipo === 'CLIENTE' ? 'Describe tus preferencias de delivery...' : 'Describe tu experiencia...'}
              multiline
              numberOfLines={4}
              textAlignVertical="top"
            />
          </View>
        </View>

        {editando && (
          <TouchableOpacity style={[styles.botonGuardar, cargando && styles.botonDeshabilitado]} onPress={guardarPerfil} disabled={cargando}>
            <Text style={styles.botonGuardarTexto}>{cargando ? 'Guardando...' : 'Guardar Cambios'}</Text>
          </TouchableOpacity>
        )}
      </View>

      <View style={styles.seccion}>
        <Text style={styles.subtitulo}>Estadísticas</Text>
        <View style={styles.estadisticas}>
          <View style={styles.estadistica}>
            <Text style={styles.estadisticaNumero}>5</Text>
            <Text style={styles.estadisticaLabel}>Pedidos</Text>
          </View>
          <View style={styles.estadistica}>
            <Text style={styles.estadisticaNumero}>4.8</Text>
            <Text style={styles.estadisticaLabel}>Calificación</Text>
          </View>
          <View style={styles.estadistica}>
            <Text style={styles.estadisticaNumero}>12</Text>
            <Text style={styles.estadisticaLabel}>Entregas</Text>
          </View>
        </View>
      </View>
    </ScrollView>
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
  },
  titulo: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#333',
  },
  botonEditar: {
    paddingHorizontal: 15,
    paddingVertical: 8,
    backgroundColor: '#007AFF',
    borderRadius: 8,
  },
  botonEditarTexto: {
    color: 'white',
    fontWeight: '500',
  },
  formContainer: {
    backgroundColor: 'white',
    margin: 15,
    borderRadius: 10,
    padding: 20,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 3,
    elevation: 2,
  },
  seccion: {
    marginBottom: 25,
  },
  subtitulo: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 15,
    color: '#333',
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
    paddingBottom: 5,
  },
  campo: {
    marginBottom: 15,
  },
  label: {
    fontSize: 14,
    fontWeight: '500',
    marginBottom: 5,
    color: '#333',
  },
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    padding: 12,
    fontSize: 16,
    backgroundColor: 'white',
  },
  inputDeshabilitado: {
    backgroundColor: '#f5f5f5',
    color: '#666',
  },
  textArea: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    padding: 12,
    fontSize: 16,
    minHeight: 100,
    textAlignVertical: 'top',
  },
  botonGuardar: {
    backgroundColor: '#28a745',
    borderRadius: 8,
    padding: 15,
    alignItems: 'center',
    marginTop: 10,
  },
  botonDeshabilitado: {
    backgroundColor: '#ccc',
  },
  botonGuardarTexto: {
    color: 'white',
    fontSize: 16,
    fontWeight: 'bold',
  },
  estadisticas: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    backgroundColor: 'white',
    padding: 20,
    borderRadius: 10,
    marginHorizontal: 15,
  },
  estadistica: {
    alignItems: 'center',
  },
  estadisticaNumero: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#007AFF',
  },
  estadisticaLabel: {
    fontSize: 12,
    color: '#666',
    marginTop: 5,
  },
});

export default PerfilScreen;