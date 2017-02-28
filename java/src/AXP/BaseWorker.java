package AXP;

import java.io.Serializable;

public class BaseWorker implements Serializable
{
    private static final long serialVersionUID = -3095373279096358080L;

    public enum WorkerStatus {
        Idle, // 线程处于无动作状态
        Running, // 线程处于运行状态
        WaitFor, // 线程处于被等待结束状态
        Disposed, // 线程处于已被释放状态
    }

    /**
     * 锁对象
     * 
     * @author Alex
     * 
     */
    public static class JLock implements Serializable
    {
        private static final long serialVersionUID = 2418881807570423199L;
    }

    /**
     * 事件对象
     * 
     * @author Alex
     * 
     */
    public static class JEvent implements Serializable
    {
        private static final long serialVersionUID = 8721348950034033173L;

        private int mCondition = 0;

        public void Wait()
        {
            while (true) {
                synchronized (this) {
                    if (mCondition != 0)
                        return;
                }

                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException e) {
                }
            }
        }

        public void Notify()
        {
            synchronized (this) {
                mCondition = 1;
            }
        }

        public JEvent Reset()
        {
            synchronized (this) {
                mCondition = 0;
                return this;
            }
        }
    }

    // 工作线程锁
    public JLock mLock = new JLock();

    // 工作线程事件
    public JEvent mEvent = new JEvent();

    // 线程名称
    public String mName = "unnamed";

    // 回放模式模式
    protected boolean mIsPlayBackMode = false;

    // 工作线程
    private Thread mThread;

    // 等待线程数量
    private int mWaitCount = 0;

    // 线程运行标志
    private boolean mRunFlag = false;

    // 线程状态
    private WorkerStatus mStatus = WorkerStatus.Idle;

    // 用户数据
    protected int mUserData = AResult.AS_OK;

    /**
     * 构造函数
     */
    public BaseWorker()
    {
    }

    /**
     * 启动函数
     * 
     * @return 启动结果,成功返回S_OK
     */
    public int Start()
    {
        synchronized (mLock) {

            if (mStatus != WorkerStatus.Idle) {
                return AResult.AE_FAIL;
            }

            mStatus = WorkerStatus.Running;
            mRunFlag = true;

            try {
                mThread = new WorkerThread();
                mThread.start();
            }
            catch (Exception e) {
                e.printStackTrace();
                return AResult.AE_FAIL;
            }
        }

        return AResult.AS_OK;
    }

    /**
     * 等待工作线程结束
     */
    public void Join()
    {
        synchronized (mLock) {
            if (mStatus == WorkerStatus.Idle) {
                return;
            }

            mStatus = WorkerStatus.WaitFor;
            mWaitCount++;
        }

        if (mThread != null) {
            while (true) {
                try {
                    mThread.join();
                    break;
                }
                catch (InterruptedException e) {
                }
            }
        }

        synchronized (mLock) {
            if (--mWaitCount > 0) {
                return;
            }

            mStatus = WorkerStatus.Idle;
        }
    }

    /**
     * 请求终止线程
     * 
     * @param userData
     *            用户参数
     */
    public void NotifyToStop(int userData)
    {
        synchronized (mLock) {
            if (mRunFlag) {
                mRunFlag = false;
                mUserData = userData;
            }
        }
    }

    /**
     * 抛弃工作线程
     */
    public void Discard()
    {
        synchronized (mLock) {
            NotifyToStop(AResult.AE_TERMINATED);
        }
    }

    /**
     * 是否终止运行
     * 
     * @return 终止运行标志
     */
    public boolean IsTerminated()
    {
        boolean ret;

        synchronized (mLock) {
            ret = (mRunFlag == false);
        }

        return ret;
    }

    /**
     * 线程是否处于无动作状态
     * 
     * @return 无动作状态标志
     */
    public boolean IsIdle()
    {
        boolean ret;

        synchronized (mLock) {
            ret = (mStatus == WorkerStatus.Idle);
        }

        return ret;
    }

    /**
     * 设置回放模式
     * 
     * @param enabled
     *            启用回放模式
     */
    public void SetPlayBackModeEnabled(boolean enabled)
    {
        synchronized (mLock) {
            mIsPlayBackMode = enabled;
        }
    }

    /**
     * 重新获取配置信息
     * 
     * @return 重新获取配置信息结果,成功返回S_OK
     */
    public int Reset()
    {
        return AResult.AS_OK;
    }

    /**
     * 准备执行函数
     */
    protected void Prepare()
    {
    }

    protected void Run()
    {
        // TODO Auto-generated method stub

    }

    private class WorkerThread extends Thread implements Serializable
    {
        private static final long serialVersionUID = 3037250322219758892L;

        /**
         * 执行函数
         */
        public void run()
        {
            Prepare();
            Run();

            synchronized (mLock) {
                if (mStatus == WorkerStatus.Running)
                    mStatus = WorkerStatus.Idle;

                NotifyToStop(AResult.AS_OK);
            }
        }
    }

    public void run()
    {
        // TODO Auto-generated method stub

    }
}