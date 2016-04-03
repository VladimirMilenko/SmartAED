using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApplication1
{
    /// <summary>
    /// Сводное описание для AED
    /// </summary>
    public class AED : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            if (StaticInfo.EnableAED)
            {
                context.Response.Write("1");
                StaticInfo.EnableAED = false;
            }
            else
            {
                context.Response.Write("0");
            }
        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}