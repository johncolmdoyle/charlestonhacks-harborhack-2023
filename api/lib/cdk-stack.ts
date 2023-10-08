import * as cdk from 'aws-cdk-lib';
import { Construct } from 'constructs';
import * as ec2 from "aws-cdk-lib/aws-ec2";
import * as ecs from "aws-cdk-lib/aws-ecs";
import * as ecs_patterns from "aws-cdk-lib/aws-ecs-patterns";
import * as route53 from 'aws-cdk-lib/aws-route53';
import * as acm from 'aws-cdk-lib/aws-certificatemanager';
import * as aws_ecr from 'aws-cdk-lib/aws-ecr';
import * as iam from 'aws-cdk-lib/aws-iam';

export class CdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    const vpc = new ec2.Vpc(this, "VPC", {
      maxAzs: 2,
      natGateways: 1
    });

    const springbootEcsCluster = new ecs.Cluster(this, "Cluster", {
      vpc,
      clusterName: "backend"
    });

    // Create a Route 53 hosted zone
    const hostedZone = route53.HostedZone.fromLookup(this, 'HostedZone', {
      domainName: 'chucktownneighbors.com',
    });

    // Create an SSL certificate
    const certificate = new acm.Certificate(this, 'ApiCertificate', {
      domainName: 'api.chucktownneighbors.com',
      validation: acm.CertificateValidation.fromDns(hostedZone),
    });

    const sesPolicy = iam.PolicyDocument.fromJson(
      {
        "Version": "2012-10-17",
        "Statement": [
            {
                "Effect": "Allow",
                "Action": [
                    "ses:*",
                ],
                "Resource": "*"
            }
        ]
    }
  );

  const sesRole = new iam.Role(this, 'SesRole', {
    assumedBy: new iam.ServicePrincipal('ecs-tasks.amazonaws.com'),
    inlinePolicies: {
        'SESAccessPolicy': sesPolicy
    }
});

    const ecrRepo = aws_ecr.Repository.fromRepositoryName(this, 'Repo', 'api');

    const springbootApp = new ecs_patterns.ApplicationLoadBalancedFargateService(this, 'ApiService', {
      cluster: springbootEcsCluster,
      desiredCount: 1,
      cpu: 256,
      memoryLimitMiB: 512,
      taskImageOptions: {
        image: ecs.ContainerImage.fromEcrRepository(ecrRepo),
        executionRole: sesRole,
        containerName: 'api',
        containerPort: 6060,
        environment: {
          'PORT': '6060',
          'CLIENT_ORIGIN_URL': 'https://app.chucktownneighbors.com',
          'OKTA_OAUTH2_ISSUER': 'https://dev-aowob8abt04txyv1.us.auth0.com/',
          'OKTA_OAUTH2_AUDIENCE': 'https://api.chucktownneighbors.com'
        },
      },
      runtimePlatform: {
        cpuArchitecture: ecs.CpuArchitecture.ARM64,
        operatingSystemFamily: ecs.OperatingSystemFamily.LINUX,
      },
      domainName: 'api.chucktownneighbors.com',
      domainZone: hostedZone,
      certificate: certificate,
      publicLoadBalancer: true,
      redirectHTTP: true
    })

    springbootApp.targetGroup.configureHealthCheck({
      "port": 'traffic-port',
      "path": '/health',
      "interval": cdk.Duration.seconds(5),
      "timeout": cdk.Duration.seconds(4),
      "healthyThresholdCount": 2,
      "unhealthyThresholdCount": 2,
      "healthyHttpCodes": "200,301,302"
    })
    //autoscaling - cpu
    const springbootAutoScaling = springbootApp.service.autoScaleTaskCount({
      maxCapacity: 6,
      minCapacity: 1
    })
    springbootAutoScaling.scaleOnCpuUtilization('CpuScaling', {
      targetUtilizationPercent: 45,
      policyName: "cpu autoscaling",
      scaleInCooldown: cdk.Duration.seconds(30),
      scaleOutCooldown: cdk.Duration.seconds(30)
    })
  }
}
