package br.pucpr.cg;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import br.pucpr.mage.Keyboard;
import br.pucpr.mage.Scene;
import br.pucpr.mage.Shader;
import br.pucpr.mage.Window;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

/**
 * Equipe: Gabriel Rieper (Rieps) e Rayane Gabrielle dos Santos Macedo (Ray)
 */
public class RotatingTriangle implements Scene {
	private Keyboard keys = Keyboard.getInstance();

	/** Esta variável guarda o identificador da malha (Vertex Array Object) do triângulo */
	private int vao;

	/** Guarda o id do shader program, após compilado e linkado */
	private int shader;

	/** Angulo que o triangulo está */
	private float angleY;

	private float angleX;

	@Override
	public void init() {
		//Ativa o teste de profundidade
		glEnable(GL_DEPTH_TEST);

		//Desliga o desenho do triangulo que estiver de costas
		glEnable(GL_CULL_FACE);

		//Define a cor de limpeza da tela
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		//------------------------------
		//Carga/Compilação dos shaders
		//------------------------------
		shader = Shader.loadProgram("basic");

		//------------------
		//Criação da malha
		//------------------

		//O processo de criação da malha envolve criar um Vertex Array Object e associar a ele um buffer, com as
		// posições dos vértices do triangulo.

		//Criação do Vertex Array Object (VAO)
		vao = glGenVertexArrays();

		//Informamos a OpenGL que iremos trabalhar com esse VAO
		glBindVertexArray(vao);

		//Criação do buffer de posições
		//------------------------------

		//v0 --- v1
		// |      |
		//v2 --- v3
		var vertexData = new float[] {
				//face1
				-0.5f,  0.5f, -0.5f,    //v0
				 0.5f,  0.5f, -0.5f,    //v1
				-0.5f, -0.5f, -0.5f,    //v2
				 0.5f, -0.5f, -0.5f,    //v3
				//face2
				 0.5f,  0.5f, -0.5f,   //v4
				 0.5f,  0.5f,  0.5f,   //v5
				 0.5f, -0.5f, -0.5f,   //v6
				 0.5f, -0.5f,  0.5f,   //v7
				//face3
				 0.5f,  0.5f,  0.5f,   //v8
				-0.5f,  0.5f,  0.5f,   //v9
				 0.5f, -0.5f,  0.5f,   //v10
				-0.5f, -0.5f,  0.5f,   //v11
				//face4
				-0.5f,  0.5f,  0.5f,   //v12
				-0.5f,  0.5f, -0.5f,   //v13
				-0.5f, -0.5f,  0.5f,   //v14
				-0.5f, -0.5f, -0.5f,   //v15
				//face5
				-0.5f,  0.5f,  0.5f,   //v16
				 0.5f,  0.5f,  0.5f,   //v17
				-0.5f,  0.5f, -0.5f,   //v18
				 0.5f,  0.5f, -0.5f,   //v19
				//face6
				 -0.5f, -0.5f, -0.5f,   //v20
				 0.5f,  -0.5f, -0.5f,   //v21
				-0.5f,  -0.5f,  0.5f,   //v22
				 0.5f,  -0.5f,  0.5f,   //v23


		};

		//Solicitamos a criação de um buffer na OpenGL, onde esse array será guardado
		var positions = glGenBuffers();
		//Informamos a OpenGL que iremos trabalhar com esse buffer
		glBindBuffer(GL_ARRAY_BUFFER, positions);

		//Damos o comando para carregar esses dados na placa de vídeo
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

		//Procuramos o identificador do atributo de posição
		var aPosition = glGetAttribLocation(shader, "aPosition");

		//Chamamos uma função que associa a posição ao buffer.
		glVertexAttribPointer(aPosition, 3, GL_FLOAT, false, 0, 0);

		//Criação do buffer de cores
		//------------------------------

		//v1 --- v2
		// |      |
		//v3 --- v4
		var colorData = new float[] {

				1.0f, 0.0f, 0.0f,  //v0
				1.0f, 0.0f, 0.0f,  //v1
				1.0f, 0.0f, 0.0f,  //v2
				1.0f, 0.0f, 0.0f,  //v3
				//
				0.0f, 1.0f, 0.0f,  //v4
				0.0f, 1.0f, 0.0f,  //v5
				0.0f, 1.0f, 0.0f,  //v6
				0.0f, 1.0f, 0.0f,  //v7
				//
				0.0f, 0.0f, 1.0f,  //v8
				0.0f, 0.0f, 1.0f,  //v9
				0.0f, 0.0f, 1.0f,  //v10
				0.0f, 0.0f, 1.0f,  //v11
				//
				1.0f, 0.0f, 1.0f,  //v12
				1.0f, 0.0f, 1.0f,  //v13
				1.0f, 0.0f, 1.0f,  //v14
				1.0f, 0.0f, 1.0f,  //v15
				//
				0.0f, 1.0f, 1.0f,  //v16
				0.0f, 1.0f, 1.0f,  //v17
				0.0f, 1.0f, 1.0f,  //v18
				0.0f, 1.0f, 1.0f,  //v19
				//
				1.0f, 1.0f, 0.0f,  //v20
				1.0f, 1.0f, 0.0f,  //v21
				1.0f, 1.0f, 0.0f,  //v22
				1.0f, 1.0f, 0.0f,  //v23


		};

		//Solicitamos a criação de um buffer na OpenGL, onde esse array será guardado
		var colors = glGenBuffers();
		//Informamos a OpenGL que iremos trabalhar com esse buffer
		glBindBuffer(GL_ARRAY_BUFFER, colors);

		//Damos o comando para carregar esses dados na placa de vídeo
		glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);

		//Informamos a OpenGL que iremos trabalhar com essa variável
		glEnableVertexAttribArray(aPosition);

		//Associação do buffer cores a variável aColor
		//---------------------------------------------------
		//Procuramos o identificador do atributo de posição
		var aColor = glGetAttribLocation(shader, "aColor");

		//Chamamos uma função que associa a cor ao shader.
		// Observe que o size mudou para 3, já que as cores são um vec3 com 3 floats para os componentes r, g e b
		glVertexAttribPointer(aColor, 3, GL_FLOAT, false, 0, 0);

		//Informamos a OpenGL que iremos trabalhar com essa variável
		glEnableVertexAttribArray(aColor);


		//Criação do index buffer
		//v0 --- v1
		// |      |
		//v2 --- v3
		var indexData = new int[] {
				//face1
				3, 1, 0,
				0, 2, 3,
				//face2
				7, 5, 4,
				4, 6, 7,
				//face3
				11, 9, 8,
				8, 10, 11,
				//face4
				15, 13, 12,
				12, 14, 15,
				//face5
				19, 17, 16,
				16, 18, 19,
				//face6
				23, 21, 20,
				20, 22, 23,




		};

		//Solicitamos a criação de um buffer na OpenGL, onde esse array será guardado
		var indices = glGenBuffers();
		//Informamos a OpenGL que iremos trabalhar com esse buffer
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indices);

		//Damos o comando para carregar esses dados na placa de vídeo
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);

		//Faxina
		//Finalizamos o nosso VAO, portanto, informamos a OpenGL que não iremos mais trabalhar com ele
		glBindVertexArray(0);

		//Como já finalizamos a carga, informamos a OpenGL que não estamos mais usando esse buffer.
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void update(float secs) {
		//Testa se a tecla ESC foi pressionada
		if (keys.isPressed(GLFW_KEY_ESCAPE)) {
			//Fecha a janela, caso tenha sido
			glfwSetWindowShouldClose(glfwGetCurrentContext(), true);

		}


		if (keys.isDown(GLFW_KEY_A)) {
			//Gira no Eixo Y em 180 Graus
			angleY += Math.toRadians(180) * secs;
			return;
		}

		if (keys.isDown(GLFW_KEY_D)) {
			//Gira no Eixo Y em -180 Graus
			angleY += Math.toRadians(-180) * secs;
			return;
		}
		if (keys.isDown(GLFW_KEY_W)) {
			//Gira no Eixo X em 180 Graus
			angleX += Math.toRadians(180) * secs;
			return;
		}
		if (keys.isDown(GLFW_KEY_S)) {
			//Gira no Eixo X em -180 Graus
			angleX += Math.toRadians(-180) * secs;
			return;
		}

	}


	@Override
	public void draw() {
		//Solicita a limpeza da tela
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		//Precisamos dizer qual VAO iremos desenhar
		glBindVertexArray(vao);

		//E qual shader program irá ser usado durante o desenho
		glUseProgram(shader);

		//Associação da variável World ao shader
		//--------------------------------------

		try (var stack = MemoryStack.stackPush()) {
			//Criamos uma matriz de rotação e a enviamos para o buffer transform
			var transform = new Matrix4f()
					.rotateY(angleY)
					.rotateX(angleX)
					.get(stack.mallocFloat(16));

			//Procuramos pelo id da variável uWorld, dentro do shader
			var uWorld = glGetUniformLocation(shader, "uWorld");

			// Copiamos os dados do buffer para a variável que está no shader
			glUniformMatrix4fv(uWorld, false, transform);
		}


		//Comandamos o desenho de 3 vértices
		glDrawElements(GL_TRIANGLES, 72, GL_UNSIGNED_INT, 0);

		//Faxina
		glBindVertexArray(0);
		glUseProgram(0);
	}

	@Override
	public void deinit() {
	}

	public static void main(String[] args) {
		new Window(new RotatingTriangle()).show();
	}
}