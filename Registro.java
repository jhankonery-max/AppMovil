import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  Alert,
  Platform
} from 'react-native';
import { NativeModules } from 'react-native';

const { UserManager } = NativeModules;

const RegistroScreen = ({ navigation }) => {
  const [formData, setFormData] = useState({
    nombreCompleto: '',
    email: '',
    telefono: '',
    password: '',
    confirmPassword: '',
    tipoUsuario: 'CLIENTE'
  });

  const validarFormulario = () => {
    if (!formData.nombreCompleto.trim()) {
      Alert.alert('Error', 'El nombre completo es obligatorio');
      return false;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      Alert.alert('Error', 'Formato de email inválido');
      return false;
    }

    if (formData.password.length < 8) {
      Alert.alert('Error', 'La contraseña debe tener mínimo 8 caracteres');
      return false;
    }

    if (!/(?=.*[a-zA-Z])(?=.*\d)/.test(formData.password)) {
      Alert.alert('Error', 'La contraseña debe incluir letras y números');
      return false;
    }

    if (formData.password !== formData.confirmPassword) {
      Alert.alert('Error', 'Las contraseñas no coinciden');
      return false;
    }

    return true;
  };

  const handleRegistro = async () => {
    if (!validarFormulario()) return;

    try {
      const resultado = await UserManager.registrarUsuario(
        formData.nombreCompleto,
        formData.email,
        formData.telefono,
        formData.password,
        formData.tipoUsuario
      );

      if (resultado.exito) {
        Alert.alert('Éxito', 'Usuario registrado correctamente');
        navigation.navigate('Login');
      } else {
        Alert.alert('Error', resultado.mensaje);
      }
    } catch (error) {
      Alert.alert('Error', 'Error en el registro: ' + error.message);
    }
  };

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.titulo}>Crear Cuenta</Text>
      
      <View style={styles.formContainer}>
        <TextInput
          style={styles.input}
          placeholder="Nombre Completo *"
          value={formData.nombreCompleto}
          onChangeText={(text) => setFormData({...formData, nombreCompleto: text})}
        />

        <TextInput
          style={styles.input}
          placeholder="Correo Electrónico *"
          keyboardType="email-address"
          autoCapitalize="none"
          value={formData.email}
          onChangeText={(text) => setFormData({...formData, email: text})}
        />

        <TextInput
          style={styles.input}
          placeholder="Teléfono (Opcional)"
          keyboardType="phone-pad"
          value={formData.telefono}
          onChangeText={(text) => setFormData({...formData, telefono: text})}
        />

        <TextInput
          style={styles.input}
          placeholder="Contraseña *"
          secureTextEntry
          value={formData.password}
          onChangeText={(text) => setFormData({...formData, password: text})}
        />

        <TextInput
          style={styles.input}
          placeholder="Confirmar Contraseña *"
          secureTextEntry
          value={formData.confirmPassword}
          onChangeText={(text) => setFormData({...formData, confirmPassword: text})}
        />

        <View style={styles.radioGroup}>
          <Text style={styles.radioLabel}>Tipo de Cuenta *</Text>
          <View style={styles.radioOptions}>
            <TouchableOpacity 
              style={styles.radioOption}
              onPress={() => setFormData({...formData, tipoUsuario: 'CLIENTE'})}
            >
              <View style={styles.radioCircle}>
                {formData.tipoUsuario === 'CLIENTE' && <View style={styles.radioSelected} />}
              </View>
              <Text>Cliente</Text>
            </TouchableOpacity>

            <TouchableOpacity 
              style={styles.radioOption}
              onPress={() => setFormData({...formData, tipoUsuario: 'REPARTIDOR'})}
            >
              <View style={styles.radioCircle}>
                {formData.tipoUsuario === 'REPARTIDOR' && <View style={styles.radioSelected} />}
              </View>
              <Text>Repartidor</Text>
            </TouchableOpacity>
          </View>
        </View>

        <TouchableOpacity style={styles.boton} onPress={handleRegistro}>
          <Text style={styles.botonTexto}>Registrarse</Text>
        </TouchableOpacity>

        <TouchableOpacity 
          style={styles.link}
          onPress={() => navigation.navigate('Login')}
        >
          <Text style={styles.linkTexto}>¿Ya tienes cuenta? Inicia Sesión</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
    padding: 20,
  },
  titulo: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
    marginVertical: 30,
    color: '#333',
  },
  formContainer: {
    backgroundColor: 'white',
    borderRadius: 10,
    padding: 20,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    padding: 15,
    marginBottom: 15,
    fontSize: 16,
  },
  radioGroup: {
    marginBottom: 20,
  },
  radioLabel: {
    fontSize: 16,
    marginBottom: 10,
    fontWeight: '500',
  },
  radioOptions: {
    flexDirection: 'row',
    justifyContent: 'space-around',
  },
  radioOption: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  radioCircle: {
    width: 20,
    height: 20,
    borderRadius: 10,
    borderWidth: 2,
    borderColor: '#007AFF',
    marginRight: 8,
    justifyContent: 'center',
    alignItems: 'center',
  },
  radioSelected: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: '#007AFF',
  },
  boton: {
    backgroundColor: '#007AFF',
    borderRadius: 8,
    padding: 15,
    alignItems: 'center',
    marginBottom: 15,
  },
  botonTexto: {
    color: 'white',
    fontSize: 18,
    fontWeight: 'bold',
  },
  link: {
    alignItems: 'center',
  },
  linkTexto: {
    color: '#007AFF',
    fontSize: 16,
  },
});

export default RegistroScreen;