package fbv;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Vector;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class Mephisto extends AdvancedRobot

{
	static int[][]				stats			= new int[13][31];
	int							direction		= 1;

	public final static int		BINS			= 47;
	public final static boolean	PAINT_MOVEMENT	= true;
	public final static boolean	PAINT_GUN		= false;
	public static double		WALL_STICK		= 160;

	public static double		mSurfStats[]	= new double[BINS];
	public Point2D.Double		mBotLocation;
	public Point2D.Double		mEnemyLocation;

	public List<Wave>			mEnemyWaves;
	public List<Integer>		mSurfDirections;
	public List<Double>			mSurfAbsBearings;

	public static double		mOppEnergy		= 100.0;

	final double				FIREPOWER		= 2;
	final double				ROBOT_WIDTH		= 16, ROBOT_HEIGHT = 16;

	final int					WALL_PROXIMITY	= 100;
	final int					QUADRANT1		= 1;
	final int					QUADRANT2		= 2;
	final int					QUADRANT3		= 3;
	final int					QUADRANT4		= 4;

	@Override
	public void run() {
		setColors(Color.BLACK, Color.BLACK, Color.RED);
		setBulletColor(Color.RED);

		mEnemyWaves = new Vector();
		mSurfDirections = new Vector();
		mSurfAbsBearings = new Vector();

		// Deixar radar e arma independente do robo
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		// Deixar o radar sempre girando
		do {
			turnRadarRightRadians(Double.POSITIVE_INFINITY);
		} while (true);

	}

	/**
	 * Método de callback chamado após radar escanear um alvo.
	 */
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		mBotLocation = new Point2D.Double(getX(), getY());

		double lateralVelocity = getVelocity() * Math.sin(e.getBearingRadians());
		double absBearingWS = e.getBearingRadians() + getHeadingRadians();

		setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearingWS - getRadarHeadingRadians()) * 2);

		mSurfDirections.add(0, new Integer((lateralVelocity >= 0) ? 1 : -1));
		mSurfAbsBearings.add(0, new Double(absBearingWS + Math.PI));

		double bulletPower = mOppEnergy - e.getEnergy();
		if (bulletPower < 3.01 && bulletPower > 0.09 && mSurfDirections.size() > 2) {
			Wave ew = new Wave();
			ew.fireTime = getTime() - 1;
			ew.bulletVelocity = bulletVelocityMethod(bulletPower);
			ew.distanceTraveled = bulletVelocityMethod(bulletPower);
			ew.direction = ((Integer) mSurfDirections.get(2)).intValue();
			ew.directAngle = ((Double) mSurfAbsBearings.get(2)).doubleValue();
			ew.fireLocation = (Point2D.Double) mEnemyLocation.clone();

			mEnemyWaves.add(ew);
		}

		mOppEnergy = e.getEnergy();

		// atualizando a localização do inimigo
		mEnemyLocation = project(mBotLocation, absBearingWS, e.getDistance());

		updateWaves();
		doSurfing();

		linearSophisticatedTargetingSystem(e);
		// trivialLinearTargetingSystem(e);
	}

	private void trivialLinearTargetingSystem(ScannedRobotEvent e) {
		double bulletPower = 3;
		double headOnBearing = getHeadingRadians() + e.getBearingRadians();
		double linearBearing = headOnBearing + Math.asin(e.getVelocity() / Rules.getBulletSpeed(bulletPower) * Math.sin(e.getHeadingRadians() - headOnBearing));
		setTurnGunRightRadians(Utils.normalRelativeAngle(linearBearing - getGunHeadingRadians()));
		setFire(bulletPower);
	}

	/**
	 * Método de mira linear avançado não iterativo, baseado no deslocamento do
	 * alvo.
	 * 
	 * Encontra a velocidade lateral do do alvo (o quão rápido o outro robô está
	 * se movendo paralelamente) Para encontrar um ângulo aproximado de
	 * compensação, assume-se que vai continuar se movendo em paralelo com a sua
	 * velocidade lateral até sua bala atingir o alvo, dando-lhe um triângulo
	 * assim:
	 * 
	 * 
	 * /| /a| bullet speed * bullet travel time / | / | /____|
	 * 
	 * lateral velocity * bullet travel time
	 * 
	 * 
	 * @param event
	 */
	private void linearSophisticatedTargetingSystem(ScannedRobotEvent event) {

		final double targetAbsBearing = getHeadingRadians() + event.getBearingRadians();
		final double robotX = getX(), robotY = getY(), bulletSpeed = Rules.getBulletSpeed(FIREPOWER);
		final double targetX = robotX + event.getDistance() * Math.sin(targetAbsBearing), targetY = robotY + event.getDistance() * Math.cos(targetAbsBearing);
		final double targetSpeed = event.getVelocity(), targetHeading = event.getHeadingRadians();

		// Constantes para facilitar o cálculo dos coeficientes quadráticos
		// abaixo
		final double A = (targetX - robotX) / bulletSpeed;
		final double B = targetSpeed / bulletSpeed * Math.sin(targetHeading);
		final double C = (targetY - robotY) / bulletSpeed;
		final double D = targetSpeed / bulletSpeed * Math.cos(targetHeading);

		// Coeficientes quadráticos: a*(1/t)^2 + b*(1/t) + c = 0
		final double a = A * A + C * C;
		final double b = 2 * (A * B + C * D);
		final double c = (B * B + D * D - 1);
		final double discrim = b * b - 4 * a * c;
		if (discrim >= 0) {

			// expressão quadrática recíproca
			final double t1 = 2 * a / (-b - Math.sqrt(discrim));
			final double t2 = 2 * a / (-b + Math.sqrt(discrim));
			final double t = Math.min(t1, t2) >= 0 ? Math.min(t1, t2) : Math.max(t1, t2);

			// Assumindo que o inimigo para nas paredes
			final double endX = limit(targetX + targetSpeed * t * Math.sin(targetHeading), ROBOT_WIDTH / 2, getBattleFieldWidth() - ROBOT_WIDTH / 2);
			final double endY = limit(targetY + targetSpeed * t * Math.cos(targetHeading), ROBOT_HEIGHT / 2, getBattleFieldHeight() - ROBOT_HEIGHT / 2);
			setTurnGunRightRadians(Utils.normalRelativeAngle(Math.atan2(endX - robotX, endY - robotY) - getGunHeadingRadians()));
			setFire(FIREPOWER);
		}
	}

	@Override
	public void onHitByBullet(HitByBulletEvent e) {
		// Se a lista de ondas inimigas estiver vazia neste momento significa
		// que o scanner perdeu ela de alguma forma
		if (!mEnemyWaves.isEmpty()) {
			Point2D.Double hitBulletLocation = new Point2D.Double(e.getBullet().getX(), e.getBullet().getY());
			Wave hitWave = null;

			// procure na lista de ondas inimigas e ache uma que poderia atingir
			// o robo
			for (int x = 0; x < mEnemyWaves.size(); x++) {

				Wave ew = (Wave) mEnemyWaves.get(x);

				if (Math.abs(ew.distanceTraveled - mBotLocation.distance(ew.fireLocation)) < 50
						&& Math.round(bulletVelocityMethod(e.getBullet().getPower()) * 10) == Math.round(ew.bulletVelocity * 10)) {
					hitWave = ew;
					break;
				}
			}

			if (hitWave != null) {
				logHit(hitWave, hitBulletLocation);

				// remova esta onda após logar os stats dela
				mEnemyWaves.remove(mEnemyWaves.lastIndexOf(hitWave));
			}
		}
	}

	/**
	 * Atualiza array de ondas
	 */
	public void updateWaves() {
		for (int x = 0; x < mEnemyWaves.size(); x++) {
			Wave ew = (Wave) mEnemyWaves.get(x);

			ew.distanceTraveled = (getTime() - ew.fireTime) * ew.bulletVelocity;
			if (ew.distanceTraveled > mBotLocation.distance(ew.fireLocation) + 50) {
				mEnemyWaves.remove(x);
				x--;
			}
		}
	}

	/**
	 * Procura onda mais próxima para surfar
	 * 
	 * @return
	 */
	public Wave getClosestSurfableWave() {
		double closestDistance = 99999;
		Wave surfWave = null;

		for (int x = 0; x < mEnemyWaves.size(); x++) {
			Wave enemyWave = (Wave) mEnemyWaves.get(x);
			double distance = mBotLocation.distance(enemyWave.fireLocation) - enemyWave.distanceTraveled;

			if (distance > enemyWave.bulletVelocity && distance < closestDistance) {
				surfWave = enemyWave;
				closestDistance = distance;
			}
		}

		return surfWave;
	}

	/**
	 * Dado uma onda inimiga e o ponto onde o bot foi atingido, atualiza a base
	 * de dados para indicar perigo naquela área
	 * 
	 * @param ew
	 * @param targetLocation
	 */
	public void logHit(Wave ew, Point2D.Double targetLocation) {
		int index = getFactorIndex(ew, targetLocation);

		for (int x = 0; x < BINS; x++) {
			// para o indice em que fomos atingidos adicione 1
			// para o indice subsequente adicione 1/2
			// para o próximo 1/5 e assim por diante
			// diminuindo o risco quanto mais distante
			mSurfStats[x] += 1.0 / (Math.pow(index - x, 2) + 1);
		}
	}

	/**
	 * Prevê a posição da bala dentro da onda e retorna indice
	 * 
	 * @param surfWave
	 * @param direction
	 * @return
	 */
	public Point2D.Double predictPosition(Wave surfWave, int direction) {

		Point2D.Double predictedPosition = (Point2D.Double) mBotLocation.clone();
		double predictedVelocity = getVelocity();
		double predictedHeading = getHeadingRadians();
		double maxTurning, moveAngle, moveDir;

		int counter = 0;
		boolean intercepted = false;

		do {
			moveAngle = wallSmoothing(predictedPosition, absoluteBearing(surfWave.fireLocation, predictedPosition) + (direction * (Math.PI / 2)), direction) - predictedHeading;
			moveDir = 1;

			if (Math.cos(moveAngle) < 0) {
				moveAngle += Math.PI;
				moveDir = -1;
			}

			moveAngle = Utils.normalRelativeAngle(moveAngle);

			// angulo máximo de escapada em um ciclo
			maxTurning = Math.PI / 720d * (40d - 3d * Math.abs(predictedVelocity));
			predictedHeading = Utils.normalRelativeAngle(predictedHeading + limit(-maxTurning, moveAngle, maxTurning));

			// se a velocidade prevista (predictedVelocity) e a direção do
			// movimento (moveDir) tem sinais diferentes
			// você deve frenar o bot, caso contrário acelerar.
			predictedVelocity += (predictedVelocity * moveDir < 0 ? 2 * moveDir : moveDir);
			predictedVelocity = limit(-8, predictedVelocity, 8);

			// calcula a nova posição prevista
			predictedPosition = project(predictedPosition, predictedHeading, predictedVelocity);

			counter++;

			// se a distância do local de tiro for menor que a distancia viajada
			// pela bala mais a quantidade de ciclos vezes a velocidade da bala
			// (ou seja o que a bala andou durante a iteração), continue a
			// iteração. Caso contrário pare e retorne a posição prevista.
			if (predictedPosition.distance(surfWave.fireLocation) < surfWave.distanceTraveled + (counter * surfWave.bulletVelocity) + surfWave.bulletVelocity) {
				intercepted = true;
			}
		} while (!intercepted && counter < 500);

		return predictedPosition;
	}

	/**
	 * Método responsável por checar perigo no array de indices de perigo
	 * 
	 * @param surfWave
	 * @param direction
	 * @return
	 */
	public double checkDanger(Wave surfWave, int direction) {
		int index = getFactorIndex(surfWave, predictPosition(surfWave, direction));

		return mSurfStats[index];
	}

	/**
	 * Método responsável pela esquiva do robô.
	 */
	public void doSurfing() {
		// Procura no array de ondas a onda mais próxima
		Wave surfWave = getClosestSurfableWave();

		if (surfWave == null) {
			return;
		}

		// Checa perigo a esquerda do robo no array de dados logados
		double dangerLeft = checkDanger(surfWave, -1);

		// Checa perigo a direita do robo no array de dados logados
		double dangerRight = checkDanger(surfWave, 1);

		// Inicializa ângulo de escape
		double goAngle = absoluteBearing(surfWave.fireLocation, mBotLocation);

		// Se o índice de perigo a esquerda for menor do que a direita
		if (dangerLeft < dangerRight) {
			// atribua o valor do angulo de escape para esquerda (evitando os
			// limites do campo de batalha)
			goAngle = wallSmoothing(mBotLocation, goAngle - (Math.PI / 2), -1);
		} else {
			// se não atribua para a direita (evitando os limites do campo de
			// batalha)
			goAngle = wallSmoothing(mBotLocation, goAngle + (Math.PI / 2), 1);
		}

		// Mude a direção do movimento para desviar
		setBackAsFront(this, goAngle);
	}

	@Override
	public void onHitRobot(HitRobotEvent event) {
		super.onHitRobot(event);

		double x = getX();
		double y = getY();
		double absBearingGun = event.getBearingRadians() + getHeadingRadians();
		double botBearing = getHeading();

		turnGunRightRadians(Utils.normalRelativeAngle(absBearingGun - getGunHeadingRadians()) * 2);
		fire(3);

		// if (botBearing < 90 && botBearing > 0) {
		// getWallProximity(x, y, QUADRANT1);
		//
		// } else if (botBearing > 90 && botBearing < 180) {
		// getWallProximity(x, y, QUADRANT2);
		//
		// } else if (botBearing > 180 && botBearing < 270) {
		// getWallProximity(x, y, QUADRANT3);
		//
		// } else if (botBearing > 0 && botBearing < 270) {
		// getWallProximity(x, y, QUADRANT4);
		// }
	}

	private void getWallProximity(double x, double y, int quadrant) {
		if (x - WALL_PROXIMITY < getBattleFieldWidth() || getBattleFieldWidth() > x + WALL_PROXIMITY) {
			ahead(100);
		} else if (y - WALL_PROXIMITY < getBattleFieldHeight() || getBattleFieldHeight() > x + WALL_PROXIMITY) {
			back(100);
		}
	}

	class Wave {

		Point2D.Double	fireLocation;
		long			fireTime;
		double			bulletVelocity;
		double			directAngle;
		double			distanceTraveled;
		int				direction;

		public Wave() {
		}

	}

	public static Rectangle2D.Double	_fieldRect	= new Rectangle2D.Double(18, 18, 764, 564);

	/**
	 * Método responsável por gerenciamento dos limites do campo de batalha.
	 * 
	 * @param botLocation
	 * @param angle
	 * @param orientation
	 * @return
	 */
	public static double wallSmoothing(Point2D.Double botLocation, double angle, int orientation) {
		while (!_fieldRect.contains(project(botLocation, angle, WALL_STICK))) {
			angle += orientation * 0.05;
		}
		return angle;
	}

	/**
	 * Dados ponto de origem, angulo de deslocamento e deslocamento. retorna o
	 * ponto onde o alvo deve se encontrar através das leis trigonométricas.
	 * 
	 * @param sourceLocation
	 * @param angle
	 * @param length
	 * @return
	 */
	public static Point2D.Double project(Point2D.Double sourceLocation, double angle, double length) {
		return new Point2D.Double(sourceLocation.x + Math.sin(angle) * length, sourceLocation.y + Math.cos(angle) * length);
	}

	/**
	 * Dados ponto de origem e destino. Retorna o valor absuluto do angulo de
	 * direcionamento (rumo).
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static double absoluteBearing(Point2D.Double source, Point2D.Double target) {
		return Math.atan2(target.x - source.x, target.y - source.y);
	}

	/**
	 * Método calcula o limite de uma função
	 * 
	 * @param min
	 * @param value
	 * @param max
	 * @return
	 */
	public static double limit(double min, double value, double max) {
		return Math.max(min, Math.min(value, max));
	}

	/**
	 * Calcula a velocidade da bala de acordo com a sua potência
	 * 
	 * @param power
	 * @return
	 */
	public static double bulletVelocityMethod(double power) {
		return (20.0 - (3.0 * power));
	}

	/**
	 * É o angulo maximo de tiro em relacao a posicao anterior do alvo. Tendo em
	 * vista a fisica do jogo.
	 * 
	 * Referências: http://robowiki.net/wiki/Maximum_Escape_Angle/Precise
	 * 
	 * Formula: http://robowiki.net/wiki/Maximum_Escape_Angle
	 * 
	 * @param velocity
	 * @return
	 */
	public static double maxEscapeAngle(double velocity) {
		return Math.asin(8.0 / velocity);
	}

	/**
	 * Inverte orientação do robo
	 * 
	 * @param robot
	 * @param goAngle
	 */
	public static void setBackAsFront(AdvancedRobot robot, double goAngle) {
		double angle = Utils.normalRelativeAngle(goAngle - robot.getHeadingRadians());
		if (Math.abs(angle) > (Math.PI / 2)) {
			if (angle < 0) {
				robot.setTurnRightRadians(Math.PI + angle);
			} else {
				robot.setTurnLeftRadians(Math.PI - angle);
			}
			robot.setBack(100);
		} else {
			if (angle < 0) {
				robot.setTurnLeftRadians(-1 * angle);
			} else {
				robot.setTurnRightRadians(angle);
			}
			robot.setAhead(100);
		}
	}

	/**
	 * Dado uma onda inimiga e as coordenadas onde o bot foi atingido, calcula o
	 * indice no vetor de status
	 * 
	 * @param enemyBulletWave
	 * @param targetLocation
	 * @return
	 */
	public static int getFactorIndex(Wave enemyBulletWave, Point2D.Double targetLocation) {
		double offsetAngle = (absoluteBearing(enemyBulletWave.fireLocation, targetLocation) - enemyBulletWave.directAngle);
		double factor = Utils.normalRelativeAngle(offsetAngle) / maxEscapeAngle(enemyBulletWave.bulletVelocity) * enemyBulletWave.direction;

		return (int) limit(0, (factor * ((BINS - 1) / 2)) + ((BINS - 1) / 2), BINS - 1);
	}

}
