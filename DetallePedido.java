import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
  KeyboardAvoidingView,
  Platform
} from 'react-native';
import { NativeModules } from 'react-native';

const { UserManager } = NativeModules;

const LoginScreen = ({ navigation }) => {
  const [credenciales, setCredenciales] = useState({
    email: '',
    password: ''
  });
  const [cargando, setCargando] = useState(false);

  const handleLogin = async () => {
    if (!credenciales.email || !credenciales.password) {
      Alert.alert('Error', 'Por favor completa todos los campos');
      return;
    }

    setCargando(true);
    try {
      const resultado = await UserManager.loginUsuario(
        credenciales.email,
        credenciales.password
      );

      if (resultado.exito) {
        Alert.alert('Éxito', 'Inicio de sesión exitoso');
        navigation.navigate('Home');
      } else {
        Alert.alert('Error', resultado.mensaje);
      }
    } catch (error) {
      Alert.alert('Error', 'Error en el login: ' + error.message);
    } finally {
      setCargando(false);
    }
  };

  return (
    <KeyboardAvoidingView 
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
    >
      <View style={styles.content}>
        <Text style={styles.titulo}>Iniciar Sesión</Text>
        
        <View style={styles.formContainer}>
          <TextInput
            style={styles.input}
            placeholder="Correo Electrónico"
            keyboardType="email-address"
            autoCapitalize="none"
            value={credenciales.email}
            onChangeText={(text) => setCredenciales({...credenciales, email: text})}
          />

          <TextInput
            style={styles.input}
            placeholder="Contraseña"
            secureTextEntry
            value={credenciales.password}
            onChangeText={(text) => setCredenciales({...credenciales, password: text})}
          />

          <TouchableOpacity 
            style={[styles.boton, cargando && styles.botonDeshabilitado]}
            onPress={handleLogin}
            disabled={cargando}
          >
            <Text style={styles.botonTexto}>
              {cargando ? 'Cargando...' : 'Iniciar Sesión'}
            </Text>
          </TouchableOpacity>

          <TouchableOpacity 
            style={styles.link}
            onPress={() => navigation.navigate('Registro')}
          >
            <Text style={styles.linkTexto}>¿No tienes cuenta? Regístrate</Text>
          </TouchableOpacity>

          <TouchableOpacity style={styles.link}>
            <Text style={styles.linkTexto}>¿Olvidaste tu contraseña?</Text>
          </TouchableOpacity>
        </View>
      </View>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    padding: 20,
  },
  titulo: {
    fontSize: 28,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 40,
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
  boton: {
    backgroundColor: '#007AFF',
    borderRadius: 8,
    padding: 15,
    alignItems: 'center',
    marginBottom: 15,
  },
  botonDeshabilitado: {
    backgroundColor: '#ccc',
  },
  botonTexto: {
    color: 'white',
    fontSize: 18,
    fontWeight: 'bold',
  },
  link: {
    alignItems: 'center',
    padding: 10,
  },
  linkTexto: {
    color: '#007AFF',
    fontSize: 16,
  },
});

export default LoginScreen;