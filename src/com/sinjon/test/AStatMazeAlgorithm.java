package com.sinjon.test;

import java.awt.List;
import java.util.ArrayList;
import java.util.Stack;

/**
 * 
 * stack中bean
 *
 */
class Step {

	int x; // 横坐标
	int y;// 纵坐标
	int d;// 方向
	int f;// 耗散值
	int g;// 走的步数 默认走一步加+1
	int h;// 启发函数
	Step lastStep;// 上一个Step

	public Step(int x, int y, int d) {
		this.x = x;// 横坐标
		this.y = y;// 纵坐标
		this.d = d;// 方向

	}
}

/**
 * 人工智能A*迷宫算法
 * 
 * @author sinjon欣荣
 *
 */
public class AStatMazeAlgorithm {
	public Stack<Step> stackOpen = new Stack<Step>();// Open表
	public Stack<Step> stackClose = new Stack<Step>();// Close表

	// 迷宫定义 终点是（1,7）起点是（7.1）
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
	// 行走试探方向 左上右下
	int[][] move = { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };

	/**
	 * 打印路径
	 * 
	 * @param stack
	 */
	public static void PrintPath(Step step) {
		System.out.println("从起点到终点的路径：");
		Stack<Step> showStack = new Stack<>();
		showStack.push(step);
		// 输出从起点到终点的完整路径
		while (step.lastStep != null) {
			showStack.push(step.lastStep);
			step = step.lastStep;
		}
		int i = 0;// 打印多次就换行
		while (!showStack.isEmpty()) {
			step = showStack.pop();

			if (step.h != 0) {// 代表不是最后一个数据
				if (i < 5) {
					System.out.print("(" + step.x + "," + step.y + ")—>");
					i++;
				} else {
					System.out.println("(" + step.x + "," + step.y + ")—>");
					i = 0;
				}
			} else {
				System.out.print("(" + step.x + "," + step.y + ")");

			}

		}
		showStack.clear();
	}

	/**
	 * 进行结果判断 能否走出迷宫
	 * 
	 * @param mazeTest
	 */
	public void isOutOfMaze(AStatMazeAlgorithm mazeTest) {

		if (mazeTest.path(maze, move)) {// 判断能否找到结果
			// 能
			Step step = (Step) stackOpen.pop();
			System.out.println("(" + step.x + "," + step.y + ")" + " 到达终点");
			System.out.println();

			PrintPath(step);

		} else {
			System.out.println("起点终点之间没有一条路径相连");
		}

		// 释放栈
		stackClose.clear();
		stackOpen.clear();

	}

	/**
	 * 走迷宫处理
	 * 
	 * @param maze
	 * @param move
	 * @param s
	 * @return
	 */
	public boolean path(int[][] maze, int[][] move) {
		Step temp = new Step(7, 1, 0);// 起点
		temp.g = 0;// 初始化栈中Step的g值 开始步数为0
		temp.lastStep = null;
		stackOpen.push(temp);// 压栈
		// 判断是否到达终点，是就返回结果
		if (temp.x == 1 && temp.y == 7) {
			return true;
		}

		while (!stackOpen.isEmpty()) {// 栈不为空
			temp = (Step) stackOpen.pop();// 弹栈
			int x = temp.x;// 横坐标
			int y = temp.y;// 纵坐标
			int d = temp.d;// 方向
			int h = computeH(x, y);// 计算启发值h(n)
			int g = temp.g;// g函数的值
			int f = h + g;// 得到f(n)的值
			temp.h = h;// 初始化栈中Step的h值
			temp.f = f;// 初始化栈中Step的f值

			while (d < 4) {// 控制探索的方向
				int i = x + move[d][0];
				int j = y + move[d][1];
				Step newPoint = new Step(i, j, 0);// 到达新点 判断是否访问过

				if (maze[i][j] == 0) { // 该点没有访问过
					g++;// 到达新节点g值自增

					int newCostValue = (computeH(i, j) + g);// 新结点的耗散值
					// 初始化新节点
					newPoint.f = newCostValue;
					newPoint.g = g;
					newPoint.h = computeH(i, j);
					newPoint.lastStep = temp;
					// 打印栈中数据
					System.out.println("(" + newPoint.x + "," + newPoint.y + ")" + " 耗散值 f:" + newCostValue + "  g:" + g
							+ "  h:" + newPoint.h);
					// 新节点入栈
					stackOpen.push(newPoint);

					g--;// 先g++再--的原因是 从一个点出发向任何一个方向只增加一步

					// 到达终点，走出迷宫，返回结果
					if (i == 1 && j == 7) {
						return true;
					}

				}
				// 所有访问过的结点都置为1不再访问
				maze[x][y] = -1;
				// 根据启发值h对栈中元素进行排序
				sortStack(stackOpen);
				// 切换方向
				d++;
			} // while end 四个方向全走完
				// 打印栈内情况
			showStack(stackOpen);
			// 访问过的节点进Close表
			stackClose.push(temp);

		}
		// 没有路径走出迷宫
		return false;
	}

	/**
	 * 耗散值的排序
	 * 
	 * @param stack
	 */
	public static void sortStack(Stack<Step> stack) {
		Stack<Step> tempStack = (Stack<Step>) stack.clone();
		ArrayList<Step> allSteps = new ArrayList<Step>();
		while (!tempStack.isEmpty()) {
			allSteps.add(tempStack.pop());
		}
		if (allSteps.size() > 1) {// 栈内对象大于1个才进行比较
			// 根据h值将Step从大到小排序
			for (int i = 0; i < allSteps.size() - 1; i++) {
				for (int j = i + 1; j < allSteps.size(); j++) {
					if (allSteps.get(i).f < allSteps.get(j).f) {
						Step temp = allSteps.get(i);
						allSteps.set(i, allSteps.get(j));
						allSteps.set(j, temp);

					}
				}
			} // for end
				// 栈的重新排序
			stack.clear();
			for (Step step : allSteps) {
				stack.push(step);
			}
		} else {
			return;
		}

	}

	/**
	 * 查看栈的情况
	 */
	public static void showStack(Stack<Step> stack) {
		Stack<Step> tempStack = (Stack<Step>) stack.clone();
		System.out.println();
		System.out.println("查看栈：");
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
	 * 计算h（N)的值
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
	 * 主函数 调用判断是否能找到一条路径
	 * @param args
	 */
	public static void main(String[] args) {
		AStatMazeAlgorithm mazeTest = new AStatMazeAlgorithm();
		System.out.println("**********************************************");
		System.out.println("人工智能作业  3115005036 计科四班 占欣荣");
		System.out.println("**********************************************");
		// 判断是否能走出迷宫并显示结果
		mazeTest.isOutOfMaze(mazeTest);

	}
}
