package com.sinjon.test;

import java.awt.List;
import java.util.ArrayList;
import java.util.Stack;

/**
 * 
 * stack��bean
 *
 */
class Step {

	int x; // ������
	int y;// ������
	int d;// ����
	int f;// ��ɢֵ
	int g;// �ߵĲ��� Ĭ����һ����+1
	int h;// ��������
	Step lastStep;// ��һ��Step

	public Step(int x, int y, int d) {
		this.x = x;// ������
		this.y = y;// ������
		this.d = d;// ����

	}
}

/**
 * �˹�����A*�Թ��㷨
 * 
 * @author sinjon����
 *
 */
public class AStatMazeAlgorithm {
	public Stack<Step> stackOpen = new Stack<Step>();// Open��
	public Stack<Step> stackClose = new Stack<Step>();// Close��

	// �Թ����� �յ��ǣ�1,7������ǣ�7.1��
	int[][] maze = { 
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
				{ 1, 0, 0, 0, 0, 0, 1, 0, 1 }, 
				{ 1, 1, 1, 0, 1, 0, 1, 0, 1 },
				{ 1, 0, 0, 0, 1, 0, 0, 0, 1 }, 
				{ 1, 0, 1, 0, 1, 0, 1, 0, 1 }, 
				{ 1, 0, 1, 0, 0, 0, 1, 0, 1 },
				{ 1, 0, 1, 0, 1, 0, 1, 0, 1 }, 
				{ 1, 0, 1, 0, 0, 0, 0, 0, 1 }, 
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1 } 
				};
	// ������̽���� ��������
	int[][] move = { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };

	/**
	 * ��ӡ·��
	 * 
	 * @param stack
	 */
	public static void PrintPath(Step step) {
		System.out.println("����㵽�յ��·����");
		Stack<Step> showStack = new Stack<>();
		showStack.push(step);
		// �������㵽�յ������·��
		while (step.lastStep != null) {
			showStack.push(step.lastStep);
			step = step.lastStep;
		}
		int i = 0;// ��ӡ��ξͻ���
		while (!showStack.isEmpty()) {
			step = showStack.pop();

			if (step.h != 0) {// ���������һ������
				if (i < 5) {
					System.out.print("(" + step.x + "," + step.y + ")��>");
					i++;
				} else {
					System.out.println("(" + step.x + "," + step.y + ")��>");
					i = 0;
				}
			} else {
				System.out.print("(" + step.x + "," + step.y + ")");

			}

		}
		showStack.clear();
	}

	/**
	 * ���н���ж� �ܷ��߳��Թ�
	 * 
	 * @param mazeTest
	 */
	public void isOutOfMaze(AStatMazeAlgorithm mazeTest) {

		if (mazeTest.path(maze, move)) {// �ж��ܷ��ҵ����
			// ��
			Step step = (Step) stackOpen.pop();
			System.out.println("(" + step.x + "," + step.y + ")" + " �����յ�");
			System.out.println();

			PrintPath(step);

		} else {
			System.out.println("����յ�֮��û��һ��·������");
		}

		// �ͷ�ջ
		stackClose.clear();
		stackOpen.clear();

	}

	/**
	 * ���Թ�����
	 * 
	 * @param maze
	 * @param move
	 * @param s
	 * @return
	 */
	public boolean path(int[][] maze, int[][] move) {
		Step temp = new Step(7, 1, 0);// ���
		temp.g = 0;// ��ʼ��ջ��Step��gֵ ��ʼ����Ϊ0
		temp.lastStep = null;
		stackOpen.push(temp);// ѹջ
		// �ж��Ƿ񵽴��յ㣬�Ǿͷ��ؽ��
		if (temp.x == 1 && temp.y == 7) {
			return true;
		}

		while (!stackOpen.isEmpty()) {// ջ��Ϊ��
			temp = (Step) stackOpen.pop();// ��ջ
			int x = temp.x;// ������
			int y = temp.y;// ������
			int d = temp.d;// ����
			int h = computeH(x, y);// ��������ֵh(n)
			int g = temp.g;// g������ֵ
			int f = h + g;// �õ�f(n)��ֵ
			temp.h = h;// ��ʼ��ջ��Step��hֵ
			temp.f = f;// ��ʼ��ջ��Step��fֵ

			while (d < 4) {// ����̽���ķ���
				int i = x + move[d][0];
				int j = y + move[d][1];
				Step newPoint = new Step(i, j, 0);// �����µ� �ж��Ƿ���ʹ�

				if (maze[i][j] == 0) { // �õ�û�з��ʹ�
					g++;// �����½ڵ�gֵ����

					int newCostValue = (computeH(i, j) + g);// �½��ĺ�ɢֵ
					// ��ʼ���½ڵ�
					newPoint.f = newCostValue;
					newPoint.g = g;
					newPoint.h = computeH(i, j);
					newPoint.lastStep = temp;
					// ��ӡջ������
					System.out.println("(" + newPoint.x + "," + newPoint.y + ")" + " ��ɢֵ f:" + newCostValue + "  g:" + g
							+ "  h:" + newPoint.h);
					// �½ڵ���ջ
					stackOpen.push(newPoint);

					g--;// ��g++��--��ԭ���� ��һ����������κ�һ������ֻ����һ��

					// �����յ㣬�߳��Թ������ؽ��
					if (i == 1 && j == 7) {
						return true;
					}

				}
				// ���з��ʹ��Ľ�㶼��Ϊ1���ٷ���
				maze[x][y] = -1;
				// ��������ֵh��ջ��Ԫ�ؽ�������
				sortStack(stackOpen);
				// �л�����
				d++;
			} // while end �ĸ�����ȫ����
				// ��ӡջ�����
			showStack(stackOpen);
			// ���ʹ��Ľڵ��Close��
			stackClose.push(temp);

		}
		// û��·���߳��Թ�
		return false;
	}

	/**
	 * ��ɢֵ������
	 * 
	 * @param stack
	 */
	public static void sortStack(Stack<Step> stack) {
		Stack<Step> tempStack = (Stack<Step>) stack.clone();
		ArrayList<Step> allSteps = new ArrayList<Step>();
		while (!tempStack.isEmpty()) {
			allSteps.add(tempStack.pop());
		}
		if (allSteps.size() > 1) {// ջ�ڶ������1���Ž��бȽ�
			// ����hֵ��Step�Ӵ�С����
			for (int i = 0; i < allSteps.size() - 1; i++) {
				for (int j = i + 1; j < allSteps.size(); j++) {
					if (allSteps.get(i).f < allSteps.get(j).f) {
						Step temp = allSteps.get(i);
						allSteps.set(i, allSteps.get(j));
						allSteps.set(j, temp);

					}
				}
			} // for end
				// ջ����������
			stack.clear();
			for (Step step : allSteps) {
				stack.push(step);
			}
		} else {
			return;
		}

	}

	/**
	 * �鿴ջ�����
	 */
	public static void showStack(Stack<Step> stack) {
		Stack<Step> tempStack = (Stack<Step>) stack.clone();
		System.out.println();
		System.out.println("�鿴ջ��");
		System.out.println("------------------------");
		while (!tempStack.isEmpty()) {
			Step step = tempStack.pop();
			System.out.print("(" + step.x + "," + step.y + ")--" + "f:" + step.f);
			System.out.println();
		}
		System.out.println("------------------------");
		System.out.println();
	}

	/**
	 * ����h��N)��ֵ
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private static int computeH(int x, int y) {
		int res = Math.abs((1 - x)) + Math.abs((7 - y));
		return res;
	}

	/**
	 * ������ �����ж��Ƿ����ҵ�һ��·��
	 * @param args
	 */
	public static void main(String[] args) {
		AStatMazeAlgorithm mazeTest = new AStatMazeAlgorithm();
		System.out.println("**********************************************");
		System.out.println("�˹�������ҵ  3115005036 �ƿ��İ� ռ����");
		System.out.println("**********************************************");
		// �ж��Ƿ����߳��Թ�����ʾ���
		mazeTest.isOutOfMaze(mazeTest);

	}
}
